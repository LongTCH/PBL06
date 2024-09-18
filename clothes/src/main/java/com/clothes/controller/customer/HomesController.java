package com.clothes.controller.customer;

import com.clothes.model.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomesController {
    @GetMapping
    public String home(Model model, HttpSession session) {
        var account = (Account) session.getAttribute("account");
        model.addAttribute("name", account != null ? account.getEmail() : "Guest");
        return "home";
    }
}
