package com.clothes.repository;

import com.clothes.model.Account;
import com.clothes.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountsRepository extends MongoRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    Page<Account> findByRole(String role, Pageable pageable);

    Page<Account> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Account> findByRoleAndFirstNameContainingIgnoreCase(String role, String firstName, Pageable pageable);
}
