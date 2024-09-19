package com.clothes.service;

import com.clothes.model.Account;

public interface AccountsService {
    Account findAccountByEmail(String username);
    Account createAccount( String firstName, String lastName, String email, String password);
}