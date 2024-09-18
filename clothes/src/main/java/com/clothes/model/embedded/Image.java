package com.clothes.model.embedded;

import java.io.Serializable;

public class Image implements Serializable {
    private String url;
    private int position;

    public Image() {
    }

    public Image(String url, int position) {
        this.url = url;
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
