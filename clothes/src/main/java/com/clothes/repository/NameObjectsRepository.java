package com.clothes.repository;

import com.clothes.model.NameObject;
import io.micrometer.common.KeyValues;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NameObjectsRepository extends MongoRepository<NameObject, String> {
}
