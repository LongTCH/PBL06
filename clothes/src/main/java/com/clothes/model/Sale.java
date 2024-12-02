package com.clothes.model;

import com.clothes.model.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@Document(collection = "sales")
public class Sale extends AuditableEntity {
    @Id
    private String id;
    private String name;
    private Integer value;
    private String status;
}
