package com.clothes.model.embedded;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Image implements Serializable {
    private String url;
    private int position;

    public Image() {
    }

    public Image(String url, int position) {
        this.url = url;
        this.position = position;
    }

}
