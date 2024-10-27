package com.clothes.controller.admin;

import com.clothes.dto.ToastMessage;
import com.clothes.model.Product;
import com.clothes.model.embedded.Image;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.CategoriesService;
import com.clothes.service.CloudinaryService;
import com.clothes.service.ProductsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller("adminProductsController")
@RequestMapping("/admin/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "size", defaultValue = "48") int size,
                                   @RequestParam(name = "categoryId", defaultValue = "all") String categoryId,
                                   @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                   Model model) {
        var categories = categoriesService.getAllCategories();
        var paginationResult = (Objects.equals(categoryId, "all"))
                ? productsService.getAllProductsWithSearch(page - 1, size, keyword)
                : productsService.getProductsByCategoryWithSearch(page - 1, size, categoryId, keyword);

        Map<String, String> categoryNames = new HashMap<>();
        for (Product product : paginationResult.getData()) {
            if (product.getCategoryId() != null) {
                String categoryName = categoriesService.getCategoryNameById(product.getCategoryId());
                categoryNames.put(product.getCategoryId(), categoryName);
            }
        }

        model.addAttribute("size", size);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        return "/admin/products/index";
    }

    @PostMapping
    public String importProducts(@RequestParam("file") MultipartFile file,
                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                 @RequestParam(name = "size", defaultValue = "48") int size,
                                 @RequestParam(name = "categoryId", defaultValue = "all") String categoryId,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                 Model model,
                                 RedirectAttributes redirectAttrs) {
        if (file.isEmpty()) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Bạn chưa chọn file."));
            return showProductsPage(page, size, categoryId, keyword, model);
        }
        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)
                && !"application/vnd.ms-excel".equals(contentType)) {
            model.addAttribute("toastMessages", new ToastMessage("error", "File tải lên không phải là định dạng Excel."));
            return showProductsPage(page, size, categoryId, keyword, model);
        }
        try {
            productsService.importProducts(file);
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Nhập sản phẩm thành công!"));
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", "Lỗi khi nhập sản phẩm: " + e.getMessage()));
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditProductPage(@PathVariable("id") ObjectId id, Model model) {
        var product = productsService.findProductById(String.valueOf(id));
        var categories = categoriesService.getAllCategories();
        var variants = product.getVariants();
        var colors = product.getColors();
        var sizes = product.getSizes();
        var price = product.getPrice();
        var getCompareAtPrice = product.getCompareAtPrice();

        String selectedCategoryId = (product.getCategoryId() != null) ? product.getCategoryId().toString() : null;

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", selectedCategoryId);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("price", price);
        model.addAttribute("getCompareAtPrice", getCompareAtPrice);
        model.addAttribute("variants", variants);

        return "/admin/products/edit";
    }

    @PostMapping("/{id}/update-info")
    public String saveProductEdit(
            @PathVariable("id") ObjectId id,
            @RequestParam String title,
            @RequestParam String categoryId,
            @RequestParam boolean status,
            @RequestParam String description,
            @RequestParam(value = "imagesToRemove", required = false) List<String> imagesToRemove,
            @RequestParam(value = "imagesToAdd", required = false) List<MultipartFile> imagesToAdd,
            RedirectAttributes redirectAttrs) {
        try {
            Product product = productsService.findProductById(String.valueOf(id));
            product.setTitle(title);
            product.setCategoryId(categoryId);
            product.setAvailable(status);
            product.setDescription(description);

            if (imagesToRemove != null) {
                product.getImages().removeIf(image -> {
                    String imageUrl = image.getUrl();
                    if (imagesToRemove.contains(imageUrl)) {
                        cloudinaryService.deleteFileByUrl(imageUrl);
                        return true;
                    }
                    return false;
                });
            }

            for (MultipartFile image : imagesToAdd) {
                if (!image.isEmpty()) {
                    Map<String, Object> uploadResult = cloudinaryService.uploadFile(image);
                    String imageUrl = (String) uploadResult.get("url");
                    int position = product.getImages().size() + 1;
                    Image newImage = new Image(imageUrl, position);
                    product.addImage(newImage);
                }
            }

            productsRepository.save(product);
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Cập nhật thông tin sản phẩm thành công!"));
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/update-colors-sizes")
    public String updateProductColorsSizes(@PathVariable("id") ObjectId id,
                                           @RequestParam(value = "colors", required = false) String[] colors,
                                           @RequestParam(value = "sizes", required = false) String[] sizes,
                                           RedirectAttributes redirectAttrs) {
        try {
            Product product = productsService.findProductById(String.valueOf(id));
            if (colors != null && colors.length > 0) {
                product.setColors(List.of(colors));
            } else {
                product.setColors(new ArrayList<>());
            }
            if (sizes != null && sizes.length > 0) {
                product.setSizes(List.of(sizes));
            } else {
                product.setSizes(new ArrayList<>());
            }

            productsService.updateVariants(product);

            productsRepository.save(product);
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Cập nhật thông tin sản phẩm thành công!"));
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/update-price-quantity-available")
    public String updateProductPriceQuantityAvailable(@PathVariable("id") ObjectId id,
                                                      @RequestParam("compareAtPrice") String[] compareAtPrices,
                                                      @RequestParam("price") String[] prices,
                                                      @RequestParam("quantity") int[] quantities,
                                                      @RequestParam("available") boolean[] available,
                                                      RedirectAttributes redirectAttrs) {
        try {
            Product product = productsService.findProductById(String.valueOf(id));
            List<ProductVariant> variants = product.getVariants();
            for (int i = 0; i < variants.size(); i++) {
                ProductVariant variant = variants.get(i);

                variant.setCompareAtPrice(Integer.parseInt(compareAtPrices[i]));
                variant.setPrice(Integer.parseInt(prices[i]));
                variant.setQuantity(quantities[i]);
                variant.setAvailable(available[i]);
            }

            productsRepository.save(product);
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Cập nhật thông tin sản phẩm thành công!"));
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") ObjectId id, RedirectAttributes redirectAttrs) {
        try {
            productsService.deleteProductById(id.toString());
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Xóa sản phẩm thành công!"));
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", "Không thể xóa sản phẩm: " + e.getMessage()));
        }
        return "redirect:/admin/products";
    }

}
