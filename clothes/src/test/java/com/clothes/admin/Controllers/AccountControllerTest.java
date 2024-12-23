package com.clothes.admin.Controllers;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.controller.admin.AccountsController;
import com.clothes.dto.PaginationResultDto;
import com.clothes.dto.ToastMessage;
import com.clothes.model.Account;
import com.clothes.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountsController accountsController;

    @Mock
    private AccountsService accountsService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        int page = 1;
        int size = 5;
        String roleId = "all";
        String keyword = "test";
        PaginationResultDto<Account> paginationResult = new PaginationResultDto<>(Collections.emptyList(), 0, 2,10);
        when(accountsService.getAllAccountsWithSearch(page - 1, size, keyword)).thenReturn(paginationResult);

        String viewName = accountsController.list(page, size, roleId, keyword, model);

        verify(model).addAttribute("roles", Arrays.asList(AccountRolesEnum.values()));
        verify(model).addAttribute("roleId", roleId);
        verify(model).addAttribute("accounts", paginationResult.getData());
        verify(model).addAttribute("currentPage", page);
        verify(model).addAttribute("size", size);
        verify(model).addAttribute("pagination", paginationResult);
        verify(model).addAttribute("keyword", keyword);
        assertEquals("admin/accounts/list", viewName);
    }

    @Test
    void testLockAccount() {
        String accountId = "123";
        String viewName = accountsController.lockAccount(accountId, model);

        verify(accountsService).lockAccount(accountId);
        assertEquals("redirect:/admin/accounts", viewName);
    }

    @Test
    void testUnlockAccount() {
        String accountId = "123";
        String viewName = accountsController.unlockAccount(accountId, model);

        verify(accountsService).unlockAccount(accountId);
        assertEquals("redirect:/admin/accounts", viewName);
    }

    @Test
    void testCreateAccount_PasswordMismatch() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        String confirmPassword = "wrongpassword";
        String role = "CUSTOMER";

        String viewName = accountsController.createAccount(firstName, lastName, email, password, confirmPassword, role, model, redirectAttributes);

        ArgumentCaptor<ToastMessage> captor = ArgumentCaptor.forClass(ToastMessage.class);
        verify(redirectAttributes).addFlashAttribute(eq("toastMessages"), captor.capture());
        ToastMessage capturedMessage = captor.getValue();
        assertEquals("error", capturedMessage.getType());
        assertEquals("Mật khẩu không khớp", capturedMessage.getContent());

        assertEquals("redirect:/admin/accounts", viewName);
    }

    @Test
    void testCreateAccount_InvalidPassword() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "123";
        String confirmPassword = "123";
        String role = "CUSTOMER";

        String viewName = accountsController.createAccount(firstName, lastName, email, password, confirmPassword, role, model, redirectAttributes);
        ArgumentCaptor<ToastMessage> captor = ArgumentCaptor.forClass(ToastMessage.class);
        verify(redirectAttributes).addFlashAttribute(eq("toastMessages"), captor.capture());
        ToastMessage capturedMessage = captor.getValue();
        assertEquals("error", capturedMessage.getType());
        assertEquals("Mật khẩu không hợp lệ. Mật khẩu cần ít nhất 6 ký tự, bao gồm chữ cái.", capturedMessage.getContent());
        assertEquals("redirect:/admin/accounts", viewName);
    }

    @Test
    void createAccount_EmailAlreadyExists() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "existing@example.com";
        String password = "password123";
        String confirmPassword = "password123";
        String role = "CUSTOMER";

        Mockito.when(accountsService.isEmailExist(email)).thenReturn(true);

        String result = accountsController.createAccount(firstName, lastName, email, password, confirmPassword, role, model, redirectAttributes);

        Mockito.verify(redirectAttributes).addFlashAttribute(
                eq("toastMessages"),
                argThat(arg -> arg instanceof ToastMessage && ((ToastMessage) arg).getType().equals("error") &&
                        ((ToastMessage) arg).getContent().equals("Email đã tồn tại, vui lòng sử dụng email khác."))
        );
        assertEquals("redirect:/admin/accounts", result);
    }

    @Test
    void testCreateAccount_Success() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        String confirmPassword = "password";
        String role = "CUSTOMER";
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPassword(password);
        account.setRole(AccountRolesEnum.valueOf(role));

        String viewName = accountsController.createAccount(firstName, lastName, email, password, confirmPassword, role, model, redirectAttributes);

        ArgumentCaptor<ToastMessage> captor = ArgumentCaptor.forClass(ToastMessage.class);
        verify(redirectAttributes).addFlashAttribute(eq("toastMessages"), captor.capture());
        ToastMessage capturedMessage = captor.getValue();
        assertEquals("success", capturedMessage.getType());
        assertEquals("Tạo tài khoản thành công", capturedMessage.getContent());
        assertEquals("redirect:/admin/accounts", viewName);
    }
}
