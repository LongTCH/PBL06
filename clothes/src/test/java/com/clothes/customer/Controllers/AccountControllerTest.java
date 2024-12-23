package com.clothes.customer.Controllers;

import com.clothes.controller.customer.AccountsController;
import com.clothes.dto.ChangePassDto;
import com.clothes.dto.ToastMessage;
import com.clothes.dto.UpdateAccountDto;
import com.clothes.model.Account;
import com.clothes.model.Order;
import com.clothes.model.embedded.Address;
import com.clothes.service.AccountsService;
import com.clothes.service.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountsController accountsController;

    @Mock
    private AccountsService accountsService;

    @Mock
    private OrdersService ordersService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private Account mockAccount;
    private List<Order> mockOrders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAccount = new Account();
        mockAccount.setEmail("test@example.com");
        mockAccount.setFirstName("John");
        mockAccount.setLastName("Doe");
        mockAccount.setPhone("123456789");
        mockOrders = new ArrayList<>();
    }

    @Test
    void testProfile() {
        when(session.getAttribute("account")).thenReturn(mockAccount);
        when(ordersService.getOrdersByAccountId(mockAccount.getEmail())).thenReturn(mockOrders);

        String viewName = accountsController.profile(model, session);

        verify(model).addAttribute("orders", mockOrders);
        verify(model).addAttribute("account", mockAccount);
        verify(model).addAttribute(eq("changePass"), any(ChangePassDto.class));

        assertEquals("customer/account/profile", viewName);
    }

    @Test
    void testUpdate_ValidInput() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(session.getAttribute("account")).thenReturn(mockAccount);
        when(accountsService.findAccountByEmail(mockAccount.getEmail())).thenReturn(mockAccount);
        UpdateAccountDto updateAccountDto = new UpdateAccountDto("John", "HaDoe", "test@example.com", "123456789", new Address());
        String viewName = accountsController.update(updateAccountDto, bindingResult, model, session);
        verify(accountsService).updateAccount(updateAccountDto);
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(session).setAttribute(eq("account"), accountCaptor.capture());
        assertEquals(mockAccount, accountCaptor.getValue());
        verify(model).addAttribute(eq("toastMessages"), any(ToastMessage.class));
        assertEquals("customer/account/profile", viewName);
    }

    @Test
    void testUpdate_InvalidInput() {
        when(bindingResult.hasErrors()).thenReturn(true);

        UpdateAccountDto updateAccountDto = new UpdateAccountDto("John", "Doe", "john.doe@example.com", "123456789", new Address());
        String viewName = accountsController.update(updateAccountDto, bindingResult, model, session);

        verify(model).addAttribute(eq("toastMessages"), any(ToastMessage.class));
        assertEquals("customer/account/profile", viewName);
    }

    @Test
    void testChangePassword_InvalidInput() {
        when(bindingResult.hasErrors()).thenReturn(true);
        ChangePassDto changePassDto = new ChangePassDto("thuha250300@gmail.com", "oldPass", "newPass", "newPass");

        String viewName = accountsController.changePassword(changePassDto, bindingResult, model, session);

        verify(model).addAttribute(eq("toastMessages"), any(ToastMessage.class));
        assertEquals("customer/account/changePass", viewName);
    }

    @Test
    void testChangePassword_MismatchPasswords() {
        when(bindingResult.hasErrors()).thenReturn(false);
        ChangePassDto changePassDto = new ChangePassDto("test@example.com", "oldPass", "newPass", "wrongConfirm");

        String viewName = accountsController.changePassword(changePassDto, bindingResult, model, session);

        verify(model).addAttribute(eq("toastMessages"), any(ToastMessage.class));
        assertEquals("customer/account/changePass", viewName);
    }

}
