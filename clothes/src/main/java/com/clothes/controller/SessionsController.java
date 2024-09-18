package com.clothes.controller;

import com.clothes.exception.AccountNotFoundException;
import com.clothes.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/new-account")
    public String getMethodName() {
        try {
            accountsService.findAccountByEmail("test@gmail.com");
        } catch (AccountNotFoundException e) {
            accountsService.createAccount("test@gmail.com", passwordEncoder.encode("123456"));
        }
        return "redirect:/sessions/login";
    }
}
