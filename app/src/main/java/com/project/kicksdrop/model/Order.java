package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order {
    private String address, coupon_id, order_create_date, order_discount, order_price, shipment_partner, shipping_price, status, user_id, order_id;
    private List<HashMap<String,String>> order_details;



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

    public String getOrder_discount() {
        return order_discount;
    }

    public void setOrder_discount(String order_discount) {
        this.order_discount = order_discount;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getShipment_partner() {
        return shipment_partner;
    }

    public void setShipment_partner(String shipment_partner) {
        this.shipment_partner = shipment_partner;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<HashMap<String, String>> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<HashMap<String, String>> order_details) {
        this.order_details = order_details;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order(String order_id, String address, String coupon_id, String order_create_date, String order_discount, String order_price,
                 String shipment_partner, String shipping_price, String status, String user_id, List<HashMap<String,String>> order_details){
        this.address = address;
        this.coupon_id = coupon_id;
        this.order_create_date = order_create_date;
        this.order_discount = order_discount;
        this.order_price = order_price;
        this.shipment_partner = shipment_partner;
        this.shipping_price = shipping_price;
        this.status = status;
        this.user_id = user_id;
        this.order_details = order_details;
        this.order_id = order_id;
    }

    public Order(){

    }





}
