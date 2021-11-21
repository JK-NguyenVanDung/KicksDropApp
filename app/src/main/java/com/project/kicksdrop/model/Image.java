package com.project.kicksdrop.model;

public class Image {
    private String image;
    private String color;
    public Image(String image, String color){
        this.image = image;
        this.color = color;
    }
    public Image(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
