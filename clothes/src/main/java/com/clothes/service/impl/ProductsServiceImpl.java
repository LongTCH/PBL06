package com.clothes.service.impl;

import com.clothes.dto.PaginationResultDto;
import com.clothes.dto.ProductExcel;
import com.clothes.model.Product;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.ExcelService;
import com.clothes.service.ProductsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsRepository productRepository;

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
    public List<Product> findRelatedProductsByCategoryId(ObjectId categoryId) {
        return productsRepository.findByCategoryId(categoryId);
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


    @Override
    @Transactional
    public void importProducts(MultipartFile file) throws Exception {
        List<ProductExcel> productExcels = excelService.readerExcelFile(file, ProductExcel.class);
        for (ProductExcel productExcel : productExcels) {
            Product product = new Product();
            product.setTitle(productExcel.getTitle());
            product.setDescription(productExcel.getDescription());
            product.setSku(productExcel.getSku());
            product.setAvailable(productExcel.isAvailable());
            product.setImages(productExcel.getImages());
            product.setVariants(productExcel.getVariants());
            product.setOptions(productExcel.getOptions());
            product.setPublishedDate(LocalDateTime.parse(productExcel.getPublishedDate()));
            product.setGroupId(new ObjectId(productExcel.getGroupId()));
            product.setCategoryId(new ObjectId(productExcel.getCategoryId()));
            productRepository.save(product);
        }

    }
}
