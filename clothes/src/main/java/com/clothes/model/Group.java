package com.clothes.model;

import com.clothes.model.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "groups")
public class Group extends AuditableEntity {
    @Id
    private String id;
    private String name;

}
