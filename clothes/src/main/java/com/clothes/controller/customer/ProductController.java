package com.clothes.controller.customer;

import com.clothes.dto.*;
import com.clothes.model.Category;
import com.clothes.model.Group;
import com.clothes.model.Product;
import com.clothes.repository.CategoriesRepository;
import com.clothes.service.CategoriesService;
import com.clothes.service.GroupsService;
import com.clothes.service.ProductsService;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("customerProductController")
@RequestMapping("/products")
public class ProductController {
    @Value("${spring.custom.predict_url}")
    private String predictUrl;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private FileDescriptorMetrics fileDescriptorMetrics;
    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(value = "/search", produces = "text/html")
    public String search(@RequestParam(required = false) String title,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "24") int size,
                         Model model) {
        var paginationResult = productsService.findProductsByTitle(title, page, size);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        return "customer/searchByTitle";
    }

    @PostMapping(value = "/search", consumes = "application/json", produces = "text/html")
    public String searchProducts(
            @RequestBody SearchDto request,
            Model model) {
        var paginationResult = productsService.findProductsByTitle(request.getTitle(), request.getPage(), request.getSize());
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", request.getPage());
        return "customer/products :: productsFragment";
    }

    @GetMapping(value = "/search-image", produces = "text/html")
    public String searchImage(Model model) {
        model.addAttribute("predictUrl", predictUrl);
        return "customer/searchByImage";
    }

    @PostMapping(value = "/search-image", consumes = "application/json", produces = "text/html")
    public String searchImageMultiCategories(
            @RequestBody PredictionsDto request,
            Model model) {
        var paginationResult = productsService.getProductsByCategoriesPrediction(request);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", request.getPage());

        return "customer/products :: productsFragment";
    }

    @GetMapping
    public String showProductsPage(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "24") int size) {
        var paginationResult = productsService.getAllProducts(page, size);
        var groups = groupsService.getAllGroups().stream()
                .distinct()
                .collect(Collectors.toList());
        var filterDto = new FiltersDto(groups);
        model.addAttribute("groups", groups);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        model.addAttribute("filters", filterDto);
        return "customer/products";
    }

    @GetMapping(value = "/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        Product product = productsService.findProductById(id);
        model.addAttribute("product", product);
        String categoryName = productsService.findCategoryNameById(product.getCategoryId());
        model.addAttribute("categoryName", categoryName);
        List<Product> relatedProducts = productsService.findRelatedProductsByCategoryId(product.getCategoryId());
        model.addAttribute("relatedProducts", relatedProducts);
        return "customer/products/ProductDetail";
    }

    @PostMapping(value = "", consumes = "application/json", produces = "text/html")
    public String filterProducts(
            @RequestBody FiltersDto filters, // Bind JSON to FiltersDto
            Model model) {

        // Get filtered products based on the incoming filters
        PaginationResultDto<Product> result = productsService.filterProducts(filters);
        // Add filtered products and pagination details to the model
        model.addAttribute("products", result.getData());
        model.addAttribute("pagination", result);
        model.addAttribute("filters", filters);

        // Return HTML fragment for products (replace 'customer/products' with the fragment if needed)
        return "customer/products :: productsFragment";
    }


    @GetMapping(value = "/group/{groupId}", produces = "text/html")
    public String filterProductsByGroupName(@PathVariable String groupId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "24") int size,
                                            Model model) {
        var groups = groupsService.getAllGroups().stream()
                .distinct()
                .collect(Collectors.toList());

        PaginationResultDto<Product> result = null;
        for (var group : groups) {
            if (group.getId().equals(groupId)) {
                result = productsService.getProductsByGroupName(groupId, page, size);
                break;
            } else {
                result = productsService.getProductsByCategoryWithSearch(page, size, groupId, "");
            }
        }

        List<Category> categories = categoriesService.getCategoryByGroupId(groupId);

        var filterDto = new FiltersDto(groups);
        model.addAttribute("group", groupId);
        model.addAttribute("products", result.getData());
        model.addAttribute("categories", categories);
        model.addAttribute("pagination", result);
        model.addAttribute("filters", filterDto);
        return "customer/products/products-categories";
    }

    @PostMapping(value = "/group/{groupId}", produces = "text/html")
    public String filterProductsByGroupName(@PathVariable String groupId,
                                            @RequestBody FiltersDto filters,
                                            Model model) {
        PaginationResultDto<Product> result;
        var group = groupsService.getGroupById(groupId);
        if (group != null) {
            filters.setGroups(List.of(new FilterSelectDto(group.getId(), group.getName(), true)));
            result = productsService.filterProducts(filters);
        } else {
            List<Category> categories = categoriesRepository.findById(groupId).stream().toList();
            Category category = categories.stream().filter(c -> c.getId().equals(groupId)).findFirst().orElse(null);
            Group groupTmp = groupsService.getGroupById(category.getGroupId());
            filters.setGroups(List.of(new FilterSelectDto(groupTmp.getId(), groupTmp.getName(), true)));
            List<Product> allProducts = new ArrayList<>();
            int currentPage = 0;
            PaginationResultDto<Product> filtered;
            do {
                filters.setPage(currentPage);
                filtered = productsService.filterProducts(filters);
                allProducts.addAll(filtered.getData());
                currentPage++;
            } while (currentPage < filtered.getTotalPages());

            List<Product> filteredProducts = allProducts.stream()
                    .filter(product -> product.getCategoryId().equals(category.getId()))
                    .collect(Collectors.toList());
            result = new PaginationResultDto<>(filteredProducts, filters.getPage(), filters.getSize(), filteredProducts.size());
        }
        model.addAttribute("products", result.getData());
        model.addAttribute("pagination", result);
        model.addAttribute("filters", filters);
        return "customer/products/products-categories :: productsFragment";
    }
}
