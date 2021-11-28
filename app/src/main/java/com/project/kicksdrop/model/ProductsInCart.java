package com.project.kicksdrop.model;

public class ProductsInCart {

    private String productID;
    private int amount;
    private String color;
    private int size;

    public ProductsInCart(String productID, int amount, String color, int size) {
        this.productID = productID;
        this.amount = amount;
        this.color = color;
        this.size = size;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
