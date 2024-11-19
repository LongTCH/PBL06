package com.clothes.model;

import com.clothes.constant.SaleStatusEnum;
import com.clothes.model.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "sales")
public class Sales extends AuditableEntity {
    @Id
    private String id;
    private String name;
    private Integer value;
    private SaleStatusEnum status;
}
