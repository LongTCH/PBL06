package com.clothes.service;

import com.clothes.dto.*;
import com.clothes.model.Category;
import com.clothes.model.Product;
import com.clothes.repository.CategoriesRepository;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.impl.ProductsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductsServiceImpl productsService;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private ExcelService excelService;

    @Mock
    private CategoriesRepository categoriesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindProductsByTitle() {
        String title = "Test Product";
        int page = 0, size = 10;
        Product product = new Product();
        product.setTitle(title);
        Page<Product> pageProduct = new PageImpl<>(List.of(product));

        when(productsRepository.findByTitleContainingIgnoreCase(eq(title), any(PageRequest.class))).thenReturn(pageProduct);

        PaginationResultDto<Product> result = productsService.findProductsByTitle(title, page, size);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(title, result.getData().get(0).getTitle());
        verify(productsRepository, times(1)).findByTitleContainingIgnoreCase(eq(title), any(PageRequest.class));
    }

    @Test
    void testGetAllProducts() {
        int page = 0, size = 10;
        Product product = new Product();
        product.setTitle("Test Product");
        Page<Product> pageProduct = new PageImpl<>(List.of(product));

        when(productsRepository.findAll(any(PageRequest.class))).thenReturn(pageProduct);

        PaginationResultDto<Product> result = productsService.getAllProducts(page, size);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("Test Product", result.getData().get(0).getTitle());
        verify(productsRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindProductById() {
        String productId = "1";
        Product product = new Product();
        product.setId(productId);

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productsService.findProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productsRepository, times(1)).findById(productId);
    }

    @Test
    void testFindCategoryById() {
        String categoryId = "123";
        String categoryName = "Test Category";

        // Create and set up a Category object
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        // Mock the repository's behavior
        when(categoriesRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method to test
        Optional<Category> result = categoriesRepository.findById(categoryId);

        // Validate the results
        assertTrue(result.isPresent(), "Expected result to be present");
        assertEquals(categoryId, result.get().getId(), "Category ID should match");
        assertEquals(categoryName, result.get().getName(), "Category name should match");
    }

}
