package com.hexaware.entity;

import com.hexaware.util.enums.ProductType;

public class Electronics extends Product{
    private String size;
    private String color;

    public Electronics(String productName, String description, double price, int quantityInStock, ProductType type, String size, String color) {
        super(productName, description, price, quantityInStock, type);
        this.size = size;
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
