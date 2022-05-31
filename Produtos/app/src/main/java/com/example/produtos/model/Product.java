package com.example.produtos.model;

import android.graphics.Bitmap;

public class Product {
    String pid;
    String name;
    String price;
    String description;
    Bitmap photo;

    public Product(String pid, String name) {
        this.pid = pid;
        this.name = name;
    }


    public Product(String name, String price, String description, Bitmap photo) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.photo = photo;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
