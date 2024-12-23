package com.clothes.repository;

import com.clothes.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface SalesRepository extends MongoRepository<Sale, String> {
}
