package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {

    private String product_id;
    private String product_brand;
    private ArrayList<String> product_colors;
    private ArrayList<HashMap<String,String>> product_images;
    private String product_name;
    private double product_price;
    private int product_quantity;
    private ArrayList<String> product_sizes;
    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;

    private String product_color;
    private String product_size;
    public Product(String product_id, String product_brand, ArrayList<String> product_colors, String product_name, double product_price, int product_quantity, ArrayList<String>  product_sizes,ArrayList<HashMap<String,String>> product_images) { //
        this.product_id = product_id;
        this.product_brand = product_brand;
        this.product_colors = product_colors;
        this.product_images = product_images;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_sizes = product_sizes;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Product(){

    }
    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public ArrayList<String> getProduct_colors() {
        return product_colors;
    }

    public void setProduct_colors(ArrayList<String>product_colors) {
        this.product_colors = product_colors;
    }

    public ArrayList<HashMap<String,String>> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ArrayList<HashMap<String,String>>  product_images) {
        this.product_images = product_images;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public ArrayList<String> getProduct_sizes() {
        return product_sizes;
    }

    public void setProduct_sizes(ArrayList<String> product_sizes) {
        this.product_sizes = product_sizes;
    }

}
