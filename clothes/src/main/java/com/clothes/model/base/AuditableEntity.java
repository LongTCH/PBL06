package com.clothes.model.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class AuditableEntity implements Serializable {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
