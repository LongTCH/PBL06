package com.clothes.service.impl;

import com.clothes.exception.AccountNotFoundException;
import com.clothes.model.Account;
import com.clothes.repository.AccountsRepository;
import com.clothes.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public Account findAccountByEmail(String username) {
        var account = accountsRepository.findByEmail(username);
        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }
        return account.get();
    }

    @Override
    public Account createAccount(String username, String password) {
        var account = new Account();
        account.setEmail(username);
        account.setPassword(password);
        return accountsRepository.save(account);
    }
}
