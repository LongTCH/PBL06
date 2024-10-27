package com.clothes.service;

import com.clothes.model.Category;

import java.util.List;

public interface CategoriesService {
    String getCategoryNameById(ObjectId categoryId);

    List<Category> getAllCategories();

    List<Category> getCategoryByGroupId(ObjectId groupId);

    String getCategoryNameById(String categoryId);

    List<Category> getAllCategories();;

    boolean existsById(String categoryId);
}
