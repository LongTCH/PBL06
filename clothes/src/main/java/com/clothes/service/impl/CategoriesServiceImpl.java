package com.clothes.service.impl;

import com.clothes.model.Category;
import com.clothes.repository.CategoriesRepository;
import com.clothes.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public String getCategoryNameById(String categoryId) {
        return categoriesRepository.findById(categoryId)
                .map(Category::getName)
                .orElse("Unknown Category");
    }

    @Override
    public List<Category> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public List<Category> getCategoryByGroupId(ObjectId groupId) {
        return categoriesRepository.findByGroupId(groupId);
        public boolean existsById(String categoryId) {
        return categoriesRepository.existsById(categoryId);
    }
}
