package com.clothes.service;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProductsService {
    PaginationResultDto<Product> findProductsByTitle(String title, int page, int size);

    PaginationResultDto<Product> getAllProducts(int page,int size);
    Product findProductById(String id);
    List<Product> findRelatedProductsByGroupId(ObjectId groupId) ;

}
