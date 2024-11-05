package com.clothes.service;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Account;

public interface AccountsService {
    Account findAccountByEmail(String username);

    Account createAccount(String firstName, String lastName, String email, String password);

    PaginationResultDto<Account> getAllAccountsWithSearch(int page, int size, String keyword);

    PaginationResultDto<Account> getAccountsByRoleWithSearch(int page, int size, String role, String keyword);

    void lockAccount(String accountId);

    void unlockAccount(String accountId);

    void createAccount(Account account);
}