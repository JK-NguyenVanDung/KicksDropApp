package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private String userID;
    private String couponID;
    List<ProductsInCart> products ;

    public Cart(String userID, String couponID, List<ProductsInCart> products) {
        this.userID = userID;
        this.couponID = couponID;
        this.products = products;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public List<ProductsInCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsInCart> products) {
        this.products = products;
    }
}
