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
    TextView amountOfGoods, totalMoneyOfGoods, productCartName, productCartType, totalPayment;
    Spinner productCartDropDownSize, productCartDropDownAmount;
    Button productCartSelectCouponHere, productCartOrder;

    List<HashMap<String, String>> products;

    public Cart(String userID, String couponID, List<HashMap<String, String>> products) {
        this.userID = userID;
        this.couponID = couponID;
        this.products = products;
    }

//    public void matching() {
//        amountOfGoods = (TextView) findViewById(R.id.productCartListViewItems_tv_amountOfGoods);
//        totalMoneyOfGoods = (TextView) findViewById(R.id.productCartListViewItems_tv_totalMoneyOfGoods);
//        productCartName = (TextView) findViewById(R.id.itemProductCart_tv_listView_name);
//        productCartType = (TextView) findViewById(R.id.itemProductCart_tv_listViewType);
//        productCartDropDownSize = (Spinner) findViewById(R.id.itemProductCart_sp_listViewDropDownSize);
//        productCartDropDownAmount = (Spinner) findViewById(R.id.itemProductCart_sp_listViewDropDownAmount);
//        productCartSelectCouponHere = (Button) findViewById(R.id.productCartListViewItems_btn_selectCouponHere);
//        productCartOrder = (Button) findViewById(R.id.productCartListViewItems_btn_order);
//        totalPayment = (TextView) findViewById(R.id.productCartListViewItems_tv_totalPayment);
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
