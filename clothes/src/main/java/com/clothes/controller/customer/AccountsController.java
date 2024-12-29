package com.clothes.controller.customer;

import com.clothes.dto.ChangePassDto;
import com.clothes.dto.ToastMessage;
import com.clothes.dto.UpdateAccountDto;
import com.clothes.model.Account;
import com.clothes.model.Order;
import com.clothes.model.Product;
import com.clothes.model.embedded.OrderItem;
import com.clothes.service.AccountsService;
import com.clothes.service.OrdersService;
import com.clothes.service.ProductsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("customerAccountsController")
@RequestMapping("/customer/account")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public String profile(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
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

    @GetMapping("/change-password")
    public String changePassword(Model model, HttpSession session) {
        var account = (Account) session.getAttribute("account");
        model.addAttribute("changePass", new ChangePassDto(account.getEmail(), "", "", ""));
        return "customer/account/changePass";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePass") ChangePassDto changePass, BindingResult result, Model model, HttpSession session) {
        var acc = (Account) session.getAttribute("account");
        if (result.hasErrors()) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Cập nhật thất bại"));
            return "customer/account/changePass";
        } else if (!changePass.getNewPassword().equals(changePass.getConfirmPassword())) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Mật khẩu mới không khớp"));
            return "customer/account/changePass";
        } else if (!passwordEncoder.matches(changePass.getOldPassword(), acc.getPassword())) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Mật khẩu hiện tại không đúng"));
            return "customer/account/changePass";
        }

        var currentAccount = (Account) session.getAttribute("account");
        var account = new ChangePassDto(currentAccount.getEmail(), changePass.getOldPassword(), changePass.getNewPassword(), changePass.getConfirmPassword());
        accountsService.changePassword(account.getEmail(), account.getNewPassword());
        model.addAttribute("toastMessages", new ToastMessage("success", "Cập nhật thành công"));
        return "customer/account/changePass";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Account account = (Account) session.getAttribute("account");
        var paginationResult = ordersService.getOrdersByAccountId(account.getEmail(), page, size);
        model.addAttribute("orders", paginationResult.getData());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        return "customer/account/orders";
    }

    @GetMapping("order/{id}")
    public String orderDetail(@PathVariable String id, Model model) {
        Order order = ordersService.findOrderById(id);

        List<String> productIds = order.getItems().stream()
                .map(OrderItem::getProductId)
                .toList();
        Map<String, Product> productsMap = productsService.findProductByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        for (OrderItem item : order.getItems()) {
            Product product = productsMap.get(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found with ID: " + item.getProductId());
            }
            item.setProduct(product);
        }

        model.addAttribute("order", order);

        return "customer/account/orderDetail";
    }
}
