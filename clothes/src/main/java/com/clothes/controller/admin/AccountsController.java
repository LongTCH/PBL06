package com.clothes.controller.admin;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.dto.ToastMessage;
import com.clothes.model.Account;
import com.clothes.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller("adminAccountsController")
@RequestMapping("/admin/accounts")
public class AccountsController {
    @Autowired
    private AccountsService accountsService;
    @GetMapping
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       @RequestParam(name = "roleId", defaultValue = "all") String roleId,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       Model model) {
        List<AccountRolesEnum> roles = Arrays.asList(AccountRolesEnum.values());
        var paginationResult = (Objects.equals(roleId, "all"))
                ? accountsService.getAllAccountsWithSearch(page - 1, size, keyword)
                : accountsService.getAccountsByRoleWithSearch(page - 1, size, roleId, keyword);
        List<Account> accounts = paginationResult.getData();
        int currentPage = page;
        model.addAttribute("roles", roles);
        model.addAttribute("roleId", roleId);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", size);
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("keyword", keyword);
        model.addAttribute("current_page", "account_active");
        return "admin/accounts/list";
    }

    @PostMapping("/lock/{id}")
    public String lockAccount(@PathVariable("id") String accountId, Model model) {
        accountsService.lockAccount(accountId);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/unlock/{id}")
    public String unlockAccount(@PathVariable("id") String accountId, Model model) {
        accountsService.unlockAccount(accountId);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/create")
    public String createAccount(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                @RequestParam String role,
                                Model model,
                                RedirectAttributes redirectAttrs) {
        if (!password.equals(confirmPassword)) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", "Mật khẩu không khớp"));
            return "redirect:/admin/accounts";
        }

        if (!isValidPassword(password)) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", "Mật khẩu không hợp lệ. Mật khẩu cần ít nhất 6 ký tự, bao gồm chữ cái."));
            return "redirect:/admin/accounts";
        }

        Account newAccount = new Account();
        newAccount.setFirstName(firstName);
        newAccount.setLastName(lastName);
        newAccount.setEmail(email);
        newAccount.setPassword(password);
        newAccount.setRole(AccountRolesEnum.valueOf(role));

        accountsService.createAccount(newAccount);
        redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Tạo tài khoản thành công"));
        List<AccountRolesEnum> roles = Arrays.asList(AccountRolesEnum.values());
        model.addAttribute("roles", roles);
        return "redirect:/admin/accounts";
    }
    private boolean isValidPassword(String password){
        if (password.length() < 6) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            return false;
        }

        return true;
    }
}
