package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    private String order_id, coupon_id, order_create_date, order_price, user_id;
    private ArrayList<HashMap<String,String>> order_details;


    public Order(String order_id, String coupon_id, String order_create_date, String order_price, String user_id, ArrayList<HashMap<String,String>> order_details){
        this.order_id = order_id;
        this.coupon_id = coupon_id;
        this.order_create_date = order_create_date;
        this.order_price = order_price;
        this.user_id = user_id;
        this.order_details = order_details;
    }

    public Order(){

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getOrder_create_date() {
        return order_create_date;
    }

    public void setOrder_create_date(String order_create_date) {
        this.order_create_date = order_create_date;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<HashMap<String, String>> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<HashMap<String, String>> order_details) {
        this.order_details = order_details;
    }



}
