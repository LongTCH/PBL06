package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ToastMessage implements Serializable {
    private String type;
    private String content;

    public ToastMessage(String type, String content) {
        this.type = type;
        this.content = content;
    }

}
