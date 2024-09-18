package com.clothes.repository;

import com.clothes.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountsRepository extends MongoRepository<Account, String> {
    Optional<Account> findByEmail(String email);
}
