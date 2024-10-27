package com.clothes.service.impl;

import com.clothes.model.Category;
import com.clothes.repository.CategoriesRepository;
import com.clothes.service.CategoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public String getCategoryNameById(ObjectId categoryId) {
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
    }
}
