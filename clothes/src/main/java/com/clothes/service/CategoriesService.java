package com.clothes.service;

import com.clothes.model.Category;

import java.util.List;

public interface CategoriesService {
    String getCategoryNameById(String categoryId);
    List<Category> getAllCategories();
}
