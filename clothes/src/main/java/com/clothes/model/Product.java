package com.clothes.model;

import com.clothes.model.embedded.Image;
import com.clothes.model.embedded.Option;
import com.clothes.model.embedded.ProductVariant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "products")
public class Product implements Serializable {
    private String id;
    private String title;
    private List<Image> images;
    private List<Option> options;
    private List<ProductVariant> variants;
    private boolean available;
    private String description;
    private String sku;
    private LocalDateTime publishedDate;
    private String groupId;
    private String categoryId;
    private String saleId;

    public List<String> getColors() {
        return options.stream()
                .filter(option -> option.getName().equalsIgnoreCase("Màu sắc"))
                .map(Option::getValues)
                .findFirst()
                .orElse(null);
    }

    public void setColors(List<String> colors) {
        Option colorOption = options.stream()
                .filter(option -> option.getName().equalsIgnoreCase("Màu sắc"))
                .findFirst()
                .orElseGet(() -> {
                    Option newOption = new Option();
                    newOption.setName("Màu sắc");
                    options.add(newOption);
                    return newOption;
                });
        colorOption.setValues(colors);
    }

    public List<String> getSizes() {
        return options.stream()
                .filter(option -> option.getName().equalsIgnoreCase("Kích thước"))
                .map(Option::getValues)
                .findFirst()
                .orElse(null);
    }

    public void setSizes(List<String> sizes) {
        Option sizeOption = options.stream()
                .filter(option -> option.getName().equalsIgnoreCase("Kích thước"))
                .findFirst()
                .orElseGet(() -> {
                    Option newOption = new Option();
                    newOption.setName("Kích thước");
                    options.add(newOption);
                    return newOption;
                });
        sizeOption.setValues(sizes);
    }

    public int getPrice() {
        return variants.isEmpty() ? 0 : variants.get(0).getPrice();
    }

    public int getCompareAtPrice() {
        return variants.isEmpty() ? 0 : variants.get(0).getCompareAtPrice();
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    public int getTotalQuantity() {
        return variants.stream().mapToInt(ProductVariant::getQuantity).sum();
    }

}
