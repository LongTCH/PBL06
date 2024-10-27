package com.clothes.service.impl;

import com.clothes.dto.PaginationResultDto;
import com.clothes.dto.ProductExcel;
import com.clothes.model.Product;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.ExcelService;
import com.clothes.service.GroupsService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsRepository productRepository;

    @Autowired
    private GroupsService groupsService;

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
    public List<Product> findRelatedProductsByCategoryId(String categoryId) {
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
        String objId = categoryId;
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
            product.setGroupId(productExcel.getGroupId());
            product.setCategoryId(productExcel.getCategoryId());
            productRepository.save(product);
        }

    }

    @Override
    public PaginationResultDto<Product> filterProducts(List<String> groupNames, List<String> sizeOptions, int minPrice, int maxPrice, int page, int size) {

        List<String> groupIds = new ArrayList<>();

        if (groupNames != null && !groupNames.isEmpty()) {
            groupIds = groupsService.getGroupIdByNames(groupNames);

            if (groupIds.isEmpty()) {
                return new PaginationResultDto<>(Collections.emptyList(), 0, page, size);
            }
        }

        List<Product> allProducts = productsRepository.findAll();
        List<String> groupIdsCopy = new ArrayList<>(groupIds);

        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> {
                    String groupIDtmp = product.getGroupId().toString();
                    return groupIdsCopy.isEmpty() || groupIdsCopy.contains(groupIDtmp);
                })
                .filter(product -> {
                    List<ProductVariant> matchingVariants = product.getVariants().stream()
                            .filter(variant -> {
                                String option2 = variant.getOptions().get(2);

                                boolean priceMatch = variant.getPrice() >= minPrice && variant.getPrice() <= maxPrice;
                                boolean sizeMatch = sizeOptions == null || sizeOptions.isEmpty() || sizeOptions.contains(option2);

                                return priceMatch && sizeMatch;
                            })
                            .collect(Collectors.toList());

                    product.setVariants(matchingVariants);
                    return !matchingVariants.isEmpty();
                })
                .collect(Collectors.toList());

        int totalProducts = filteredProducts.size();
        int start = Math.min(page * size, totalProducts);
        int end = Math.min(start + size, totalProducts);
        List<Product> paginatedProducts = filteredProducts.subList(start, end);

        return new PaginationResultDto<>(paginatedProducts, totalProducts, page, size);
    }

    @Override
    public List<Product> findProductByIds(List<String> productIds) {
        return productsRepository.findAllById(productIds);
    }
}

    @Override
    public void updateVariants(Product product) {
        List<ProductVariant> newVariants = new ArrayList<>();
        for (String color : product.getColors()) {
            for (String size : product.getSizes()) {
                ProductVariant newVariant = new ProductVariant();
                newVariant.setName(color + " / " + size);
                newVariant.setOptions(Map.of(1, color, 2, size));
                newVariant.setPrice(product.getPrice());
                newVariant.setCompareAtPrice(product.getCompareAtPrice());
                newVariant.setQuantity(0);
                newVariant.setAvailable(true);
                newVariants.add(newVariant);
            }
        }
        product.setVariants(newVariants);
    }

    public void deleteProductById(String id) {
        productsRepository.deleteById(id);
    }
}
