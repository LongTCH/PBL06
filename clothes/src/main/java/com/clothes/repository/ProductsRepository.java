package com.clothes.repository;

import com.clothes.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductsRepository extends MongoRepository<Product, String> {
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
