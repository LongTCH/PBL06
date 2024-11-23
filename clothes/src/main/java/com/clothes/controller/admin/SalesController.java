package com.clothes.controller.admin;

import com.clothes.dto.SaleRequest;
import com.clothes.model.Category;
import com.clothes.model.Product;
import com.clothes.model.Sale;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.repository.SalesRepository;
import com.clothes.service.CategoriesService;
import com.clothes.service.GroupsService;
import com.clothes.service.ProductsService;
import com.clothes.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminSalesController")
@RequestMapping("/admin/sales")
public class SalesController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private ProductsService productRepository;
    @Autowired
    private SalesService salesService;

    @RequestMapping("/create")
    public String listProduct(@RequestParam(required = false) String groupId,
                       @RequestParam(required = false) String categoryId,
                       @RequestParam(required = false) String search,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
        var groups = groupsService.getAllGroups();
        List<Category> categories = (groupId != null) ? categoriesService.getCategoryByGroupId(groupId) : categoriesService.getAllCategories();

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productsService.findFilteredProducts(groupId, categoryId, search, pageable);

        Map<String, String> categoryNames = new HashMap<>();
        Map<String, String> groupNames = new HashMap<>();

        for (Product product : productPage.getContent()) {
            if (product.getCategoryId() != null) {
                String categoryName = categoriesService.getCategoryNameById(product.getCategoryId());
                categoryNames.put(product.getCategoryId(), categoryName);
            }
            if (product.getGroupId() != null) {
                String groupName = groupsService.getGroupNameById(product.getGroupId());
                groupNames.put(product.getGroupId(), groupName);
            }
        }

        model.addAttribute("current_page", "sale_active");
        model.addAttribute("groups", groups);
        model.addAttribute("categories", categories);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("groupNames", groupNames);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("search", search);
        model.addAttribute("groupId", groupId);
        return "/admin/sales/create";
    }

    @PostMapping("/createSale")
    public ResponseEntity<?> createSale(@RequestBody SaleRequest saleRequest) {
        List<Product> products = productRepository.findProductByIds(saleRequest.getProductIds());

        List<Product> conflictingProducts = products.stream()
                .filter(product -> product.getSaleId() != null)
                .toList();

        if (!conflictingProducts.isEmpty() && (saleRequest.getOverrideConflict() == null || !saleRequest.getOverrideConflict())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message", "Một số sản phẩm đang thuộc sale khác. Bạn có muốn chuyển chúng sang sale mới không?",
                            "conflictingProducts", conflictingProducts
                    ));
        }

        Sale sale = new Sale();
        sale.setName(saleRequest.getName());
        sale.setValue(saleRequest.getValue());
        sale.setStatus(saleRequest.getStatus());
        salesRepository.save(sale);

        for (Product product : products) {
            product.setSaleId(sale.getId());
            for (ProductVariant variant : product.getVariants()) {
                variant.setCompareAtPrice(variant.getCompareAtPrice());
                variant.setPrice((int) (variant.getCompareAtPrice() * (1 - sale.getValue() / 100)));
            }
        }
        productRepository.saveAllProducts(products);
        return ResponseEntity.ok(Map.of("message", "Sale được tạo thành công!", "sale", sale));
    }

    @GetMapping("/list")
    public String listSales(Model model) {
        List<Sale> sales = salesRepository.findAll();
        model.addAttribute("sales", sales);
        return "/admin/sales/list";
    }
    @PostMapping("/{id}/close")
    public String closeSale(@PathVariable("id") String saleId) {
        salesService.closeSale(saleId);
        return "redirect:/admin/sales/list";
    }
    @PostMapping("/{id}/open")
    public String openSale(@PathVariable("id") String saleId) {
        salesService.openSale(saleId);
        return "redirect:/admin/sales/list";
    }
    @PostMapping("/{id}/delete")
    public String deleteSale(@PathVariable("id") String saleId) {
        salesService.deleteSale(saleId);
        return "redirect:/admin/sales/list";
    }
    @GetMapping("/{id}/products")
    public String viewProductsInSale(@PathVariable("id") String saleId, Model model) {
        List<Product> products = productsService.getProductsBySaleId(saleId);
        model.addAttribute("products", products);
        model.addAttribute("saleId", saleId);
        return "/admin/sales/sale-products";
    }
    @PostMapping("/{saleId}/products/{productId}/remove")
    public String removeProductFromSale(@PathVariable String saleId, @PathVariable String productId, RedirectAttributes redirectAttributes) {
        boolean success = productsService.removeProductFromSale(productId);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm khỏi khuyến mãi thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Xóa sản phẩm thất bại!");
        }
        return "redirect:/admin/sales/" + saleId + "/products";
    }
}
