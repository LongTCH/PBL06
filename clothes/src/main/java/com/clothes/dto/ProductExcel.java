package com.clothes.dto;

import com.clothes.model.embedded.Image;
import com.clothes.model.embedded.Option;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.service.ExcelService;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ProductExcel implements ExcelReader<ProductExcel> {
    private String title;
    private List<Image> images;
    private List<Option> options;
    private List<ProductVariant> variants;
    private boolean available;
    private String description;
    private String sku;
    private String publishedDate;
    private String groupId;
    private String categoryId;

    @Override
    public ProductExcel fromRow(Row row) {
        this.title = ExcelService.getCellValueAsString(row.getCell(0));
        this.description = ExcelService.getCellValueAsString(row.getCell(1));
        this.sku = ExcelService.getCellValueAsString(row.getCell(2));
        this.available = Boolean.parseBoolean(ExcelService.getCellValueAsString(row.getCell(3)));
        this.publishedDate = String.valueOf(LocalDateTime.now());

        String imageUrls = ExcelService.getCellValueAsString(row.getCell(4));
        this.images = new ArrayList<>();
        if (imageUrls != null) {
            String[] urls = imageUrls.split(",");
            int position = 1;
            for (String url : urls) {
                this.images.add(new Image(url, position));
                position++;
            }
        }

        this.options = new ArrayList<>();
        String colorValues = ExcelService.getCellValueAsString(row.getCell(5));
        String sizeValues = ExcelService.getCellValueAsString(row.getCell(6));
        if (colorValues != null) {
            Option colorOption = new Option();
            colorOption.setName("Màu sắc");
            colorOption.setValues(Arrays.asList(colorValues.split(",")));
            colorOption.setPosition(1);
            this.options.add(colorOption);
        }
        if (sizeValues != null) {
            Option sizeOption = new Option();
            sizeOption.setName("Kích thước");
            sizeOption.setValues(Arrays.asList(sizeValues.split(",")));
            sizeOption.setPosition(2);
            this.options.add(sizeOption);
        }

        this.variants = new ArrayList<>();
        String variantData = ExcelService.getCellValueAsString(row.getCell(7));
        if (variantData != null) {
            String[] variantRows = variantData.split(";");
            for (String variantRow : variantRows) {
                String[] variantParts = variantRow.split(",");
                ProductVariant variant = new ProductVariant();
                variant.setId(new ObjectId().toHexString());
                variant.setName(variantParts[0].trim());
                variant.setPrice(Integer.parseInt(variantParts[1].trim()));
                variant.setCompareAtPrice(Integer.parseInt(variantParts[2].trim()));
                variant.setAvailable(Boolean.parseBoolean(variantParts[3].trim()));
                variant.setQuantity(Integer.parseInt(variantParts[4].trim()));

                for (int i = 5; i < variantParts.length; i++) {
                    String[] optionParts = variantParts[i].split(":");
                    variant.getOptions().put(Integer.valueOf(optionParts[0].trim()), optionParts[1].trim());
                }
                this.variants.add(variant);
            }
        }

        this.groupId = ExcelService.getCellValueAsString(row.getCell(8));
        this.categoryId = ExcelService.getCellValueAsString(row.getCell(9));
        return this;
    }
}
