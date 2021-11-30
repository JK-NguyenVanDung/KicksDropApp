package com.project.kicksdrop.model;

public class Coupon {
    private String coupon_id;
    private String coupon_code;
    private String coupon_duration;
    private int coupon_max_price;

    private int coupon_min_price;
    private String coupon_name;
    private String coupon_percent;
    private String coupon_time;
    private String coupon_type;


    public boolean getCoupon_checked() {
        return coupon_checked;
    }

    public void setCoupon_checked(boolean coupon_checked) {
        this.coupon_checked = coupon_checked;
    }

    private boolean coupon_checked;


    public Coupon(String coupon_id, String coupon_code, String coupon_duration, int coupon_max_price, String coupon_name, String coupon_percent, String coupon_time){
        this.coupon_id = coupon_id;
        this.coupon_code = coupon_code;
        this.coupon_duration = coupon_duration;
        this.coupon_max_price = coupon_max_price;
        this.coupon_name = coupon_name;
        this.coupon_percent = coupon_percent;
        this.coupon_time = coupon_time;
    }


    public int getCoupon_min_price() {
        return coupon_min_price;
    }

    public void setCoupon_min_price(int coupon_min_price) {
        this.coupon_min_price = coupon_min_price;
    }

    public Coupon(){

    }
    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }


    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }
    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_duration() {
        return coupon_duration;
    }

    public void setCoupon_duration(String coupon_duration) {
        this.coupon_duration = coupon_duration;
    }

    public int getCoupon_max_price() {
        return coupon_max_price;
    }

    public void setCoupon_max_price(int coupon_max_price) {
        this.coupon_max_price = coupon_max_price;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_percent() {
        return coupon_percent;
    }

    public void setCoupon_percent(String coupon_percent) {
        this.coupon_percent = coupon_percent;
    }

    public String getCoupon_time() {
        return coupon_time;
    }

    public void setCoupon_time(String coupon_time) {
        this.coupon_time = coupon_time;
    }

}
