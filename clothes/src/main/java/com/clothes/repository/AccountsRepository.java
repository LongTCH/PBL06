package com.clothes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import com.clothes.model.Account;

public interface AccountsRepository extends MongoRepository<Account, String> {
    Optional<Account> findByEmail(String email);
}
