package com.clothes.repository;

import com.clothes.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface SalesRepository extends MongoRepository<Sale, String> {
    @Query("{ 'status' : 'open' }")
    List<Sale> findByStatus(String status);
}
