// src/main/java/com/test/mvc/web/controller/PledgeController.java
package com.test.mvc.web.controller;

import com.test.mvc.model.service.PledgeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class PledgeController {

    private final PledgeService pledgeService;

    /**
     * รับฟอร์มสนับสนุนโครงการ
     */
    @PostMapping("/projects/{projectId}/pledge")
    public String createPledge(@PathVariable Long projectId,
                               @RequestParam BigDecimal amount,
                               @RequestParam(required = false) Long rewardTierId,
                               HttpSession session,
                               RedirectAttributes ra) {

        Long userId = (Long) session.getAttribute("LOGIN_USER_ID"); // ใช้ผู้ใช้ที่ล็อกอิน

        var result = pledgeService.pledge(userId, projectId, rewardTierId, amount);

        // ส่ง flash message กลับหน้าเดิม
        if (result.approved()) {
            ra.addFlashAttribute("msg", result.message());
        } else {
            ra.addFlashAttribute("error", result.message());
        }
        return "redirect:/home/project/" + projectId;
    }
}
