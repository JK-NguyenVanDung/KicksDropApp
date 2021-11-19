package com.project.kicksdrop.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {

    private String product_id;
    private String product_brand;
    private HashMap<String,String> product_colors;
    private ArrayList<HashMap<String,String>> product_images;
    private String product_name;
    private int product_price;
    private int product_quantity;
    private HashMap<String,String> product_size;

    public Product(String product_id, String product_brand, HashMap<String,String> product_colors, String product_name, int product_price, int product_quantity, HashMap<String,String>  product_size,ArrayList<HashMap<String,String>> product_images) { //
        this.product_id = product_id;
        this.product_brand = product_brand;
        this.product_colors = product_colors;
        this.product_images = product_images;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_size = product_size;
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

    public HashMap<String,String> getProduct_colors() {
        return product_colors;
    }

    public void setProduct_colors(HashMap<String,String> product_colors) {
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

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public HashMap<String,String> getProduct_size() {
        return product_size;
    }

    public void setProduct_size(HashMap<String,String>  product_size) {
        this.product_size = product_size;
    }

}
