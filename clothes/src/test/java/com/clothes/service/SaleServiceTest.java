package com.clothes.service;

import com.clothes.model.Product;
import com.clothes.model.Sale;
import com.clothes.repository.ProductsRepository;
import com.clothes.repository.SalesRepository;
import com.clothes.service.impl.SalesServiceImpl;
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

class SaleServiceTest {

    @InjectMocks
    private SalesServiceImpl salesService;

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCloseSale() {
        String saleId = "sale123";
        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus("open");

        when(salesRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(salesRepository.save(sale)).thenReturn(sale);

        salesService.closeSale(saleId);

        assertEquals("closed", sale.getStatus());
        verify(salesRepository).save(sale);
    }

    @Test
    void testOpenSale() {
        String saleId = "sale123";
        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus("closed");

        when(salesRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(salesRepository.save(sale)).thenReturn(sale);

        salesService.openSale(saleId);

        assertEquals("open", sale.getStatus());
        verify(salesRepository).save(sale);
    }

    @Test
    void testDeleteSale() {
        String saleId = "sale123";
        Sale sale = new Sale();
        sale.setId(saleId);

        Product product1 = new Product();
        product1.setId("prod1");
        product1.setSaleId(saleId);

        Product product2 = new Product();
        product2.setId("prod2");
        product2.setSaleId(saleId);

        List<Product> products = Arrays.asList(product1, product2);

        when(salesRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(productsRepository.findBySaleId(saleId)).thenReturn(products);

        salesService.deleteSale(saleId);

        verify(productsRepository, times(1)).save(product1);
        verify(productsRepository, times(1)).save(product2);
        verify(salesRepository, times(1)).delete(sale);
    }

    @Test
    void testGetActiveSales() {
        Sale sale1 = new Sale();
        sale1.setId("sale1");
        sale1.setStatus("open");

        Sale sale2 = new Sale();
        sale2.setId("sale2");
        sale2.setStatus("open");

        List<Sale> activeSales = Arrays.asList(sale1, sale2);

        when(salesRepository.findByStatus("open")).thenReturn(activeSales);

        List<Sale> result = salesService.getActiveSales();

        assertEquals(2, result.size());
        assertTrue(result.contains(sale1));
        assertTrue(result.contains(sale2));
    }

    @Test
    void testGet() {
        String saleId = "sale123";
        Sale sale = new Sale();
        sale.setId(saleId);

        when(salesRepository.findById(saleId)).thenReturn(Optional.of(sale));

        Sale result = salesService.get(saleId);

        assertNotNull(result);
        assertEquals(saleId, result.getId());
    }
}
