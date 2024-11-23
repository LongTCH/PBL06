package com.clothes.controller.customer;

import com.clothes.dto.ChangePassDto;
import com.clothes.dto.ToastMessage;
import com.clothes.dto.UpdateAccountDto;
import com.clothes.model.Account;
import com.clothes.model.Order;
import com.clothes.service.AccountsService;
import com.clothes.service.OrdersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("customerAccountsController")
@RequestMapping("/customer/account")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public String profile(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        List<Order> orders = ordersService.getOrdersByAccountId(account.getEmail());
        model.addAttribute("orders", orders);
        model.addAttribute("account", account);
        model.addAttribute("changePass", new ChangePassDto(account.getEmail(), "", "", ""));
        return "customer/account/profile";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("account") UpdateAccountDto account, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Cập nhật thất bại"));
            return "customer/account/profile";
        }
        var currentAccount = (Account) session.getAttribute("account");
        account.setEmail(currentAccount.getEmail());
        accountsService.updateAccount(account);
        session.setAttribute("account", accountsService.findAccountByEmail(currentAccount.getEmail()));
        model.addAttribute("toastMessages", new ToastMessage("success", "Cập nhật thành công"));
        return "customer/account/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePass") ChangePassDto changePass, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Cập nhật thất bại"));
            return "customer/account/profile";
        } else if (!changePass.getNewPassword().equals(changePass.getConfirmPassword())) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Mật khẩu mới không khớp"));
            return "customer/account/profile";
        } else if (!accountsService.findAccountByEmail(changePass.getEmail()).getPassword().equals(changePass.getOldPassword())) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Mật khẩu cũ không đúng"));
            return "customer/account/profile";
        }

        var currentAccount = (Account) session.getAttribute("account");
        var account = new ChangePassDto(currentAccount.getEmail(), changePass.getOldPassword(), changePass.getNewPassword(), changePass.getConfirmPassword());
        accountsService.changePassword(account.getEmail(), account.getNewPassword());
        model.addAttribute("toastMessages", new ToastMessage("success", "Cập nhật thành công"));
        return "customer/account/profile";
    }


}
