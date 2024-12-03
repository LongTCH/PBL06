package com.clothes.controller;

import com.clothes.config.SecurityConfig;
import com.clothes.dto.ToastMessage;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sessions")
public class SessionsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfig securityConfig;

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
                                @RequestParam String firstName, @RequestParam String lastName, Model model,
                                RedirectAttributes redirectAttrs) {
        try {
            accountsService.findAccountByEmail(email);
            model.addAttribute("errorMessage", "Tài khoản đã tồn tại trong hệ thống");
            return "register";
        } catch (AccountNotFoundException e) {
            try {
                securityConfig.registerUser(email,password);
                accountsService.createAccount(firstName, lastName, email, passwordEncoder.encode(password));
                redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Đăng ký tài khoản thành công!"));
                return "redirect:/sessions/login";
            } catch (IllegalArgumentException ex) {
                model.addAttribute("errorMessage", ex.getMessage());
                return "register";
            }
        }
    }
}
