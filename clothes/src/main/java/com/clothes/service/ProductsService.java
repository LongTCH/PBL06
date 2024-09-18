package com.clothes.service;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;

public interface ProductsService {
    PaginationResultDto<Product> findProductsByTitle(String title, int page, int size);
}
