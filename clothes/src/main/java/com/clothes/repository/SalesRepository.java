package com.clothes.repository;

import com.clothes.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SalesRepository extends MongoRepository<Sale, String> {
}
