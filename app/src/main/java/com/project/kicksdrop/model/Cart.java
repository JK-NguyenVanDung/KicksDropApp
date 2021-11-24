package com.project.kicksdrop.model;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {

    private String userID;
    private String couponID;

    List<HashMap<String, String>> products;

    public Cart(String userID, String couponID, List<HashMap<String, String>> products) {
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

    public List<HashMap<String, String>> getProducts() {
        return products;
    }

    public void setProducts(List<HashMap<String, String>> products) {
        this.products = products;
    }
}
