package com.clothes.customer.Controllers;

import com.clothes.controller.customer.CartController;
import com.clothes.model.Product;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.service.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    @Mock
    private ProductsService productsService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testGetCartPage() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/products/cart"));
    }

    @Test
    void testGetVariantByIdAndName_Found() throws Exception {
        // Mock data
        Product product = new Product();
        product.setId("1");
        ProductVariant variant = new ProductVariant();
        variant.setName("Variant 1");
        product.setVariants(Arrays.asList(variant));
        // Mock service
        when(productsService.findProductById("1")).thenReturn(product);

        mockMvc.perform(get("/cart/variants")
                        .param("id", "1")
                        .param("variantName", "Variant 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Variant 1"));
    }

    @Test
    void testGetVariantByIdAndName_NotFound() throws Exception {
        // Mock data
        when(productsService.findProductById("1")).thenReturn(null);

        mockMvc.perform(get("/cart/variants")
                        .param("id", "1")
                        .param("variantName", "Variant 1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetVariantByIdAndName_VariantNotFound() throws Exception {
        // Giả sử bạn mock phương thức findProductById để trả về một product có variants là null
        Product mockProduct = new Product();
        mockProduct.setVariants(null); // variants là null

        when(productsService.findProductById("some-id")).thenReturn(mockProduct);

        mockMvc.perform(get("/variants")
                        .param("id", "some-id")
                        .param("variantName", "variantName"))
                .andExpect(status().isNotFound()); // Kiểm tra trả về 404 nếu variants là null
    }


}
