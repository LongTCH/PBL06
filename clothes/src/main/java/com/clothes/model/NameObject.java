package com.clothes.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "name_objects")
public class NameObject {
    @Id
    private String id;
    private Object value;
}
