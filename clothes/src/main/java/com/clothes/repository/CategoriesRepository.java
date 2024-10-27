package com.clothes.repository;

import com.clothes.model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends MongoRepository<Category, ObjectId> {
    Optional<Category> findById(ObjectId id);

    List<Category> findByGroupId(ObjectId groupId);
}
