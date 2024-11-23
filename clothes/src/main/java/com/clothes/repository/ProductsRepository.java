package com.clothes.repository;

import com.clothes.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends MongoRepository<Product, String> {
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<Product> findById(String id);

    List<Product> findByCategoryId(String categoryId);

    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByCategoryIdAndTitleContainingIgnoreCase(String categoryId, String title, Pageable pageable);

    Page<Product> findByGroupId(String groupId, Pageable pageable);

    @Query("{ 'variants.price': { $gte: ?0, $lte: ?1 }, 'groupId': { $in: ?2 } }")
    Page<Product> filterProductsByPriceRangeAndGroupIds(double minPrice, double maxPrice, List<String> groupIds, Pageable pageable);

    Page<Product> findByCategoryIdIn(List<String> categoryIds, Pageable pageable);
}
