package com.clothes.model;

import com.clothes.model.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category extends AuditableEntity {
    @Id
    private String id;
    private String name;
    private String groupId;

}
