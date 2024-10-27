package com.clothes.service;

import com.clothes.model.Category;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoriesService {
    String getCategoryNameById(ObjectId categoryId);

    List<Category> getAllCategories();

    List<Category> getCategoryByGroupId(ObjectId groupId);
}
