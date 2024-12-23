package com.clothes.service.impl;

import com.clothes.dto.*;
import com.clothes.model.Product;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.repository.ProductsRepository;
import com.clothes.repository.SalesRepository;
import com.clothes.service.ExcelService;
import com.clothes.service.GroupsService;
import com.clothes.service.NameObjectsService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
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
    private SalesRepository salesRepository;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private NameObjectsService nameObjectsService;

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
    public PaginationResultDto<Product> filterProducts(FiltersDto filtersDto) {
        boolean isAtLeastOneGroupSelected = filtersDto.getGroups().stream().anyMatch(FilterSelectDto::getSelected);
        List<String> groupIds = isAtLeastOneGroupSelected ? filtersDto.getGroups().stream()
                .filter(FilterSelectDto::getSelected)
                .map(FilterSelectDto::getId)
                .collect(Collectors.toList()) : filtersDto.getGroups().stream()
                .map(FilterSelectDto::getId)
                .collect(Collectors.toList());
        Page<Product> paginatedProducts = productsRepository.filterProductsByPriceRangeAndGroupIds(filtersDto.getMinPrice(), filtersDto.getMaxPrice(), groupIds, PageRequest.of(filtersDto.getPage(), filtersDto.getSize()));

        return new PaginationResultDto<>(paginatedProducts.getContent(), filtersDto.getPage(), paginatedProducts.getTotalPages(), paginatedProducts.getTotalElements());
    }

    @Override
    public List<Product> findProductByIds(List<String> productIds) {
        return productsRepository.findAllById(productIds);
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

    @Override
    public PaginationResultDto<Product> getProductsByGroupName(String groupId, int page, int size) {
        var pageProduct = productsRepository.findByGroupId(groupId, PageRequest.of(page, size));
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }

    @Override
    public PaginationResultDto<Product> getProductsByCategoriesPrediction(PredictionsDto predictionsDto) {
        var predictions = predictionsDto.getPredictions();
        var page = predictionsDto.getPage();
        var size = predictionsDto.getSize();
        var listCategoryIds = nameObjectsService.getPredictionCategoryIds(predictions);
        int numProductPerCategory = size / listCategoryIds.size();
        var listNumProductPerCategory = new ArrayList<Integer>();
        for (int i = 0; i < listCategoryIds.size() - 1; i++) {
            listNumProductPerCategory.add(numProductPerCategory);
        }
        listNumProductPerCategory.add(size - numProductPerCategory * (listCategoryIds.size() - 1));
        var listPageProduct = new ArrayList<PaginationResultDto<Product>>();
        for (int i = 0; i < listCategoryIds.size(); i++) {
            var pageProduct = productsRepository.findByCategoryId(listCategoryIds.get(i), PageRequest.of(page, listNumProductPerCategory.get(i)));
            listPageProduct.add(new PaginationResultDto<>(pageProduct.getContent(), page, pageProduct.getTotalPages(), pageProduct.getTotalElements()));
        }
        var totalPageProduct = listPageProduct.get(0);
        for (int i = 1; i < listPageProduct.size(); i++) {
            totalPageProduct = totalPageProduct.plus(listPageProduct.get(i));
        }
        return totalPageProduct;
    }

    public Page<Product> findFilteredProducts(String group, String category, String search, Pageable pageable) {
        List<Product> products = productRepository.findAll();

        if (group != null && !group.isEmpty()) {
            products = products.stream()
                    .filter(product -> group.equals(product.getGroupId()))
                    .collect(Collectors.toList());
        }

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(product -> category.equals(product.getCategoryId()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isEmpty()) {
            products = products.stream()
                    .filter(product -> {
                        boolean matchesFullId = product.getId().toLowerCase().contains(search.toLowerCase());
                        boolean matchesLastPart = product.getId().toLowerCase().endsWith(search.toLowerCase());
                        boolean matchesWithPrefix = search.toLowerCase().startsWith("prod-") &&
                                product.getId().toLowerCase().contains(search.substring(5).toLowerCase());
                        boolean matchesTitle = product.getTitle().toLowerCase().contains(search.toLowerCase());
                        return matchesFullId || matchesLastPart || matchesWithPrefix || matchesTitle;
                    })
                    .collect(Collectors.toList());
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());

        if (start > products.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, products.size());
        }

        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }


    @Override
    public List<Product> findAllProducts() {
        return productsRepository.findAll();
    }

    public void saveAllProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public List<Product> getProductsBySaleId(String saleId) {
        return productRepository.findBySaleId(saleId);
    }

    @Override
    public boolean removeProductFromSale(String productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            for (ProductVariant variant : product.getVariants()) {
                variant.setPrice(variant.getCompareAtPrice());
            }
            product.setSaleId(null);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getProductSales() {
        return productsRepository.findAllBySaleIdIsNotNull();
    }

    @Override
    public PaginationResultDto<Product> getProductsPaginationBySaleId(String saleId, int page, int size) {
        var pageProduct = productsRepository.findBySaleId(saleId, PageRequest.of(page, size));
        var products = pageProduct.getContent();
        return new PaginationResultDto<>(products, page, pageProduct.getTotalPages(), pageProduct.getTotalElements());
    }
}
