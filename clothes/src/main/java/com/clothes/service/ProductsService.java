package com.clothes.service;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductsService {
    PaginationResultDto<Product> findProductsByTitle(String title, int page, int size);

    PaginationResultDto<Product> getAllProducts(int page, int size);


    Product findProductById(String id);

    List<Product> findRelatedProductsByCategoryId(ObjectId category);

    PaginationResultDto<Product> getAllProductsWithSearch(int page, int size, String keyword);

    PaginationResultDto<Product> getProductsByCategoryWithSearch(int page, int size, String categoryId, String keyword);

    void importProducts(MultipartFile file) throws Exception;

    List<Product> findProductByIds(List<String> productIds);

    PaginationResultDto<Product> filterProducts(List<String> groupNames, List<String> sizeOptions, int minPrice, int maxPrice, int page, int size);

}