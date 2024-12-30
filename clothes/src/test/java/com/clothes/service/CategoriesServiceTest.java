package com.clothes.service;

import com.clothes.model.Category;
import com.clothes.repository.CategoriesRepository;
import com.clothes.service.impl.CategoriesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriesServiceTest {

    @Mock
    private CategoriesRepository categoriesRepository;

    @InjectMocks
    private CategoriesServiceImpl categoriesService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("1", "Clothes","1");
    }

    @Test
    void testGetCategoryNameById_Found() {
        // Arrange
        when(categoriesRepository.findById("1")).thenReturn(Optional.of(category));

        // Act
        String categoryName = categoriesService.getCategoryNameById("1");

        // Assert
        assertEquals("Clothes", categoryName);
        verify(categoriesRepository, times(1)).findById("1");
    }

    @Test
    void testGetCategoryNameById_NotFound() {
        // Arrange
        when(categoriesRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        String categoryName = categoriesService.getCategoryNameById("1");

        // Assert
        assertEquals("Unknown Category", categoryName);
        verify(categoriesRepository, times(1)).findById("1");
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category("1", "Clothes","1");
        Category category2 = new Category("2", "Accessories","2");
        when(categoriesRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<Category> categories = categoriesService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Clothes", categories.get(0).getName());
        assertEquals("Accessories", categories.get(1).getName());
        verify(categoriesRepository, times(1)).findAll();
    }

    @Test
    void testExistsById_True() {
        // Arrange
        when(categoriesRepository.existsById("1")).thenReturn(true);

        // Act
        boolean exists = categoriesService.existsById("1");

        // Assert
        assertTrue(exists);
        verify(categoriesRepository, times(1)).existsById("1");
    }

    @Test
    void testExistsById_False() {
        // Arrange
        when(categoriesRepository.existsById("1")).thenReturn(false);

        // Act
        boolean exists = categoriesService.existsById("1");

        // Assert
        assertFalse(exists);
        verify(categoriesRepository, times(1)).existsById("1");
    }

    @Test
    void testGetCategoryByGroupId() {
        // Arrange
        Category category1 = new Category("1", "Clothes","1");
        Category category2 = new Category("2", "Accessories", "2");
        when(categoriesRepository.findByGroupId("group1")).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<Category> categories = categoriesService.getCategoryByGroupId("group1");

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Clothes", categories.get(0).getName());
        assertEquals("Accessories", categories.get(1).getName());
        verify(categoriesRepository, times(1)).findByGroupId("group1");
    }
}
