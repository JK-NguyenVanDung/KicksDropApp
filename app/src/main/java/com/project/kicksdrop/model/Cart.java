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

    //product cart
    TextView totalItems, totalMoney, productCartName, productCartType, totalPayment;
    Spinner productCartDropDownSize, productCartDropDownAmount;
    Button productCartSelectCouponHere, productCartOrder;
    ImageButton productCartColor;

    List<HashMap<String, String>> products;

    public Cart(String userID, String couponID, List<HashMap<String, String>> products) {
        this.userID = userID;
        this.couponID = couponID;
        this.products = products;
    }

//    public void matching() {
//        totalItems = (TextView) findViewById(R.id.cartListView_tv_totalItems);
//        totalMoney = (TextView) findViewById(R.id.cartListView_tv_totalMoney);
//        productCartName = (TextView) findViewById(R.id.cartListView_tv_name);
//        productCartType = (TextView) findViewById(R.id.cartListView_tv_type);
//        productCartDropDownSize = (Spinner) findViewById(R.id.cartListView_spinner_dropDownSize);
//        productCartDropDownAmount = (Spinner) findViewById(R.id.cartListView_spinner_dropDownAmount);
//        productCartSelectCouponHere = (Button) findViewById(R.id.cartListView_btn_selectCoupon);
//        productCartColor = (ImageButton) findViewById(R.id.cartListView_ibtn_yellow);
//        productCartOrder = (Button) findViewById(R.id.cartListView_btn_order);
//        totalPayment = (TextView) findViewById(R.id.cartListView_tv_payment);
//    }

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
