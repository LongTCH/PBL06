package com.clothes.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashBoardController {
    @GetMapping
    public String getMethodName(Model model, HttpSession session) {
        model.addAttribute("name", "Admin");
        return "admin/home";
    }
}
