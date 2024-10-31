package com.clothes.repository;

import com.clothes.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends MongoRepository<Category, String> {
    Optional<Category> findById(String id);

    List<Category> findByGroupId(String groupId);
}
