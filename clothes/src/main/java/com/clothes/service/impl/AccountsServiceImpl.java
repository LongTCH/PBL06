package com.clothes.service.impl;

import com.clothes.dto.PaginationResultDto;
import com.clothes.dto.UpdateAccountDto;
import com.clothes.exception.AccountNotFoundException;
import com.clothes.model.Account;
import com.clothes.repository.AccountsRepository;
import com.clothes.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public Account createAccount(String firstName, String lastName, String email, String password) {
        var account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPassword(password);
        return accountsRepository.save(account);
    }

    @Override
    public PaginationResultDto<Account> getAllAccountsWithSearch(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> pageAccount;
        if (keyword == null || keyword.isEmpty()) {
            pageAccount = accountsRepository.findAll(pageable);
        } else {
            pageAccount = accountsRepository.findByFirstNameContainingIgnoreCase(keyword, pageable);
        }
        var accounts = pageAccount.getContent();
        return new PaginationResultDto<>(accounts, page, pageAccount.getTotalPages(), pageAccount.getTotalElements());
    }

    @Override
    public PaginationResultDto<Account> getAccountsByRoleWithSearch(int page, int size, String role, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> pageProduct;
        if (keyword != null && !keyword.isEmpty()) {
            pageProduct = accountsRepository.findByRoleAndFirstNameContainingIgnoreCase(role, keyword, pageable);
        } else {
            pageProduct = accountsRepository.findByRole(role, pageable);
        }
        var accounts = pageProduct.getContent();
        return new PaginationResultDto<>(accounts, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }

    public void lockAccount(String accountId) {
        Account account = accountsRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setLocked(true);
        accountsRepository.save(account);
    }

    public void unlockAccount(String accountId) {
        Account account = accountsRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setLocked(false);
        accountsRepository.save(account);
    }

    @Override
    public void createAccount(Account account) {
        account.setPassword(encodePassword(account.getPassword()));
        accountsRepository.save(account);
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public Account updateAccount(UpdateAccountDto account) {
        var accountToUpdate = accountsRepository.findByEmail(account.getEmail()).orElseThrow(() -> new AccountNotFoundException());
        System.out.println(accountToUpdate);
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setPhone(account.getPhone());
        accountToUpdate.setAddress(account.getAddress());
        return accountsRepository.save(accountToUpdate);
    }

    @Override
    public void changePassword(String email, String password) {
        Account account = accountsRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException());
        account.setPassword(encodePassword(password));
        account.setPassword(encodePassword(password));
        accountsRepository.save(account);
    }

}
