// src/main/java/com/test/mvc/model/service/impl/PledgeServiceImpl.java
package com.test.mvc.model.service.impl;

import com.test.mvc.model.entity.*;
import com.test.mvc.model.repository.*;
import com.test.mvc.model.service.PledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PledgeServiceImpl implements PledgeService {

    private final ProjectRepository projectRepo;
    private final RewardTierRepository rewardRepo;
    private final PledgeRepository pledgeRepo;
    private final UserRepository userRepo;
    private final PledgeHistoryRepository historyRepo;

    @Override
    @Transactional
    public Result pledge(Long userId, Long projectId, Long rewardTierId, BigDecimal amount) {
        User user = (userId == null) ? null : userRepo.findById(Math.toIntExact(userId)).orElse(null);
        Project project = projectRepo.findById(projectId).orElseThrow();

        // Rule #0: ต้องเลือกระดับรางวัลเสมอ
        if (rewardTierId == null) {
            recordReject(user, project, null, amount, "Reward required");
            return new Result(false, "กรุณาเลือกระดับรางวัล");
        }

        // Rule #1: deadline ต้องอยู่อนาคต
        if (project.getDeadline().isBefore(OffsetDateTime.now())) {
            recordReject(user, project, null, amount, "Deadline passed");
            return new Result(false, "โครงการหมดเขตแล้ว");
        }

        // ดึงรางวัลแบบ lock เพื่ออัปเดตโควตาให้ชัวร์
        RewardTier tier = rewardRepo.lockById(rewardTierId).orElseThrow();

        // ต้องเป็น reward ของ project เดียวกัน
        if (!tier.getProject().getId().equals(projectId)) {
            recordReject(user, project, null, amount, "Reward not in project");
            return new Result(false, "เลือกรางวัลไม่ถูกต้อง");
        }

        // Rule #2: จำนวนเงินต้อง >= ขั้นต่ำ
        if (amount.compareTo(tier.getMinAmount()) < 0) {
            recordReject(user, project, tier, amount, "Amount below minimum");
            incReject(user);
            return new Result(false, "ยอดเงินน้อยกว่าขั้นต่ำของรางวัล");
        }

        // Rule #3: โควตาต้อง > 0 ถ้ามีการกำหนด
        if (tier.getQuota() != null && tier.getQuota() <= 0) {
            recordReject(user, project, tier, amount, "Quota is zero");
            incReject(user);
            return new Result(false, "โควตารางวัลหมด");
        }

        // APPROVE → บันทึก pledge + อัปเดตยอด + ลดโควตา
        Pledge p = Pledge.builder()
                .project(project)
                .rewardTier(tier)
                .user(user)
                .supporterName(user != null ? user.getUsername() : "guest")
                .amount(amount)
                .pledgedAt(OffsetDateTime.now())
                .build();
        pledgeRepo.save(p);

        project.setCurrentBalance(project.getCurrentBalance().add(amount));
        projectRepo.save(project);

        if (tier.getQuota() != null) {
            tier.setQuota(tier.getQuota() - 1);
            rewardRepo.save(tier); // ด้วย lock จะหักได้ทุกครั้ง
        }

        historyRepo.save(PledgeHistory.builder()
                .user(user).project(project).rewardTier(tier)
                .amount(amount).status("APPROVED").pledge(p).reason(null).build());

        // (ถ้ามีตัวนับ approve ใน user ให้บวกตรงนี้ด้วย)
        if (user != null) {
            user.setApproveCount((user.getApproveCount() == null ? 0 : user.getApproveCount()) + 1);
            userRepo.save(user);
        }

        return new Result(true, "ขอบคุณสำหรับการสนับสนุน!");
    }

    // ===== helpers =====
    private void recordReject(User u, Project pj, RewardTier t, BigDecimal amt, String reason) {
        historyRepo.save(PledgeHistory.builder()
                .user(u).project(pj).rewardTier(t)
                .amount(amt).status("REJECTED").reason(reason).build());
        incReject(u);
    }

    private void incReject(User u) {
        if (u != null) {
            u.setRejectCount(u.getRejectCount() + 1);
            userRepo.save(u);
        }
    }
}
