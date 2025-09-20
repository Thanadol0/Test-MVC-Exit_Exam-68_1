package com.test.mvc.web.controller;


import com.test.mvc.model.repository.PledgeHistoryRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class SummaryController {


    private final PledgeHistoryRepository historyRepo;

    @GetMapping("/summary")
    public String mySummary(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("LOGIN_USER_ID");
        if (userId == null) return "redirect:/login";

        long approved = historyRepo.countByUser_IdAndStatus(userId, "APPROVED");
        long rejected = historyRepo.countByUser_IdAndStatus(userId, "REJECTED");
        long total    = historyRepo.countByUser_Id(userId);

        model.addAttribute("approvedPledges", approved);
        model.addAttribute("failedPledges", rejected);
        model.addAttribute("totalPledges", total);
        model.addAttribute("projectStats", historyRepo.summarizePerProject(userId));
        model.addAttribute("histories", historyRepo.findByUser_IdOrderByCreatedAtDesc(userId));

        // เผื่ออยากโชว์ชื่อผู้ใช้บนหน้า
        model.addAttribute("username", session.getAttribute("LOGIN_USER"));

        return "summary"; // templates/summary.html
    }

}
