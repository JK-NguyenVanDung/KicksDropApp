package com.project.kicksdrop.model;

public class ProductImage {
    private String color;
    private String image;
    public ProductImage(String color, String image) {
        this.color = color;
        this.image = image;
    }
    public ProductImage(){

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
