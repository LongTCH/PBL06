package com.clothes.service.impl;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import com.clothes.repository.CategoriesRepository;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.ProductsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public PaginationResultDto<Product> findProductsByTitle(String title, int page, int size) {
        var pageProduct = productsRepository.findByTitleContainingIgnoreCase(title == null ? "" : title, PageRequest.of(page, size));
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }

    @Override
    public PaginationResultDto<Product> getAllProducts(int page, int size) {
        var pageProduct = productsRepository.findAll(PageRequest.of(page, size));
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }

    @Override
    public Product findProductById(String id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findRelatedProductsByGroupId(ObjectId groupId) {
        return productsRepository.findByGroupId(groupId);
    }

    @Override
    public PaginationResultDto<Product> getAllProductsWithSearch(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageProduct;
        if (keyword == null || keyword.isEmpty()) {
            pageProduct = productsRepository.findAll(pageable);
        } else {
            pageProduct = productsRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }

    @Override
    public PaginationResultDto<Product> getProductsByCategoryWithSearch(int page, int size, String categoryId, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        ObjectId objId = new ObjectId(categoryId);
        Page<Product> pageProduct;
        if (keyword != null && !keyword.isEmpty()) {
            pageProduct = productsRepository.findByCategoryIdAndTitleContainingIgnoreCase(objId, keyword, pageable);
        } else {
            pageProduct = productsRepository.findByCategoryId(objId, pageable);
        }
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }
}
