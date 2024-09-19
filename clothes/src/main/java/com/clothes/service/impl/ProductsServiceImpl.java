package com.clothes.service.impl;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

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
}
