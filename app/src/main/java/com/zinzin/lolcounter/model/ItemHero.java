package com.zinzin.lolcounter.model;

import java.util.ArrayList;
import java.util.List;

public class ItemHero {
    String name;
    String baseName;
    String url;
    String url_image;
    List<String> item = new ArrayList<>();

    public ItemHero(String name, String baseName, String url, String url_image) {
        this.name = name;
        this.baseName = baseName;
        this.url = url;
        this.url_image = url_image;
    }

    public ItemHero() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }
}
