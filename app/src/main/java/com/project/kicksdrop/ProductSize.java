package com.project.kicksdrop;

public class ProductSize{
    private String sizeProduct;

    public ProductSize(String sizeProduct){
        this.sizeProduct = sizeProduct;
    }

    public String getSizeProduct(){
        return sizeProduct;
    }

    public void setSizeProduct(String sizeProduct){
        this.sizeProduct = sizeProduct;
    }

    public String toString(){
        return this.getSizeProduct();
    }

}