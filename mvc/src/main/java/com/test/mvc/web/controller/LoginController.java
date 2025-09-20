package com.test.mvc.web.controller;

import com.test.mvc.model.service.LoginService;
import com.test.mvc.web.webdto.LoginForm;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("form", new LoginForm("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("form") LoginForm form,
                          HttpSession session,
                          Model model) {

        return loginService.authenticate(form.username(), form.password())
                .map(user -> {
                    session.setAttribute("LOGIN_USER", user.getUsername());
                    session.setAttribute("LOGIN_USER_ID", user.getId());
                    return "redirect:/home";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
                    return "login";
                });
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}


