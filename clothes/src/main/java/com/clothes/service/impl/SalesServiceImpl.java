package com.clothes.service.impl;

import com.clothes.model.Product;
import com.clothes.model.Sale;
import com.clothes.repository.ProductsRepository;
import com.clothes.repository.SalesRepository;
import com.clothes.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    private SalesRepository saleRepository;
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public void closeSale(String saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        sale.setStatus("closed");
        saleRepository.save(sale);
    }

    @Override
    public void openSale(String saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        sale.setStatus("open");
        saleRepository.save(sale);
    }

    @Override
    public void deleteSale(String saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        List<Product> products = productsRepository.findBySaleId(saleId);
        for (Product product : products) {
            product.setSaleId(null);
            productsRepository.save(product);
        }
        saleRepository.delete(sale);
    }

    @Override
    public List<Sale> getActiveSales() {
        return saleRepository.findByStatus("open");
    }

    @Override
    public Sale get(String saleId) {
        return saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}
