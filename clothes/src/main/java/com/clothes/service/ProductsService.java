package com.clothes.service;

import com.clothes.dto.FiltersDto;
import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductsService {
    PaginationResultDto<Product> findProductsByTitle(String title, int page, int size);

    PaginationResultDto<Product> getAllProducts(int page, int size);

    Product findProductById(String id);

    List<Product> findRelatedProductsByCategoryId(String category);

    PaginationResultDto<Product> getAllProductsWithSearch(int page, int size, String keyword);

    PaginationResultDto<Product> getProductsByCategoryWithSearch(int page, int size, String categoryId, String keyword);

    void importProducts(MultipartFile file) throws Exception;

    List<Product> findProductByIds(List<String> productIds);

    PaginationResultDto<Product> filterProducts(FiltersDto filtersDto);

    void updateVariants(Product product);

    void deleteProductById(String id);

    List<Product> findAllProducts();

    PaginationResultDto<Product> getProductsByGroupName(String groupId, int page, int size);

    Page<Product> findFilteredProducts(String groupId, String categoryId, String search, Pageable pageable);

    void saveAllProducts(List<Product> products);

    List<Product> getProductsBySaleId(String saleId);

    boolean removeProductFromSale(String productId);
}
