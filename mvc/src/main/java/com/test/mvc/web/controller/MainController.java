// src/main/java/com/test/mvc/web/controller/PageController.java
package com.test.mvc.web.controller;

import com.test.mvc.model.entity.Project;
import com.test.mvc.model.repository.CategoryRepository;
import com.test.mvc.model.repository.ProjectRepository;
import com.test.mvc.model.repository.RewardTierRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProjectRepository projectRepo;
    private final CategoryRepository categoryRepo;
    private final RewardTierRepository rewardRepo;

    @GetMapping("/home")
    public String home(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sort
    ) {
        if (session.getAttribute("LOGIN_USER") == null) return "redirect:/login";

        // map ค่าจาก dropdown เป็น Sort
        Sort sortObj = switch (sort == null ? "" : sort) {
            case "newest"     -> Sort.by(Sort.Direction.DESC, "id");              // ใหม่สุด
            case "deadline"   -> Sort.by(Sort.Direction.ASC,  "deadline");        // ใกล้หมดเวลา
            case "mostFunded" -> Sort.by(Sort.Direction.DESC, "currentBalance");  // ระดมได้มากสุด
            default           -> Sort.by(Sort.Direction.ASC,  "id");
        };

        String keyword = (q == null) ? "" : q.trim();

        var projects = (categoryId != null && categoryId > 0)
                ? projectRepo.findByCategory_IdAndNameContainingIgnoreCase(categoryId, keyword, sortObj)
                : projectRepo.findByNameContainingIgnoreCase(keyword, sortObj);

        model.addAttribute("projects", projects);
        model.addAttribute("categories", categoryRepo.findAll());

        // คืนค่ากลับให้ฟอร์มจำ state
        model.addAttribute("q", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);

        return "main"; // templates/main.html
    }

    // หน้ารายละเอียดโครงการ (เช่น /home/project/1)
    @GetMapping("/home/project/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("LOGIN_USER") == null) return "redirect:/login";

        Project p = projectRepo.findById(id).orElseThrow();  // 404 ถ้าไม่พบ
        model.addAttribute("project", p);
        model.addAttribute("rewardTiers", rewardRepo.findAll()
                .stream().filter(t -> t.getProject().getId().equals(id)).toList());
        return "detail"; // templates/detail.html
    }
}
