package com.clothes.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class DashBoardController {
    @GetMapping
    public String getMethodName(Model model, HttpSession session) {
        model.addAttribute("name", "Admin");
        return "home";
    }
}
