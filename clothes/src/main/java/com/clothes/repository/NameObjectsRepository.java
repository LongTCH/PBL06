package com.clothes.repository;

import com.clothes.model.NameObject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NameObjectsRepository extends MongoRepository<NameObject, String> {
}
