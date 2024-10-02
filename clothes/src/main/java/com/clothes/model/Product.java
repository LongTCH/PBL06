package com.clothes.model;

import com.clothes.model.embedded.Image;
import com.clothes.model.embedded.Option;
import com.clothes.model.embedded.ProductVariant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
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
    private ObjectId groupId;
    private ObjectId categoryId;
}
