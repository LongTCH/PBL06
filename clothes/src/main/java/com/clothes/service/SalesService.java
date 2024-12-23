package com.clothes.service;

import com.clothes.model.Sale;

import java.util.List;

public interface SalesService {
    void closeSale(String saleId);

    void openSale(String saleId);

    void deleteSale(String saleId);

    List<Sale> getActiveSales();

    Sale get(String saleId);
}
