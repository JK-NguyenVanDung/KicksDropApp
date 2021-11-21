package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {

    private String idUser;
    private HashMap<String, String > coupon;
    private String email;
    private String gender;
    private String mobile;
    private String name;
    private String pass;
    private String userName;
    private ArrayList<Product> wishlist;

    public Account(String idUser, HashMap<String, String> coupon, String email, String gender, String mobile, String name, String pass, String userName, ArrayList<Product> wishlist) {
        this.idUser = idUser;
        this.coupon = coupon;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.name = name;
        this.pass = pass;
        this.userName = userName;
        this.wishlist = wishlist;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public HashMap<String, String> getCoupon() {
        return coupon;
    }

    public void setCoupon(HashMap<String, String> coupon) {
        this.coupon = coupon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Product> wishlist) {
        this.wishlist = wishlist;
    }
}
