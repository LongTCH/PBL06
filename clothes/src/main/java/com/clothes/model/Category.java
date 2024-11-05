package com.clothes.model;

import com.clothes.model.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "categories")
public class Category extends AuditableEntity {
    @Id
    private String id;
    private String name;
    private String groupId;

}
