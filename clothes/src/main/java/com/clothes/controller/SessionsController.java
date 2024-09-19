package com.clothes.controller;

import com.clothes.exception.AccountNotFoundException;
import com.clothes.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/sessions")
public class SessionsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/new-account")
    public String getMethodName(@RequestParam String email, @RequestParam String password,
                                @RequestParam String firstName, @RequestParam String lastName, Model model) {
        try {
            accountsService.findAccountByEmail(email);
            model.addAttribute("errorMessage","Tài khoản đã tồn tại trong hệ thống");
            return "register";
        } catch (AccountNotFoundException e) {
            accountsService.createAccount(firstName, lastName, email, passwordEncoder.encode(password));
            return "redirect:/sessions/login";
        }
    }
}
