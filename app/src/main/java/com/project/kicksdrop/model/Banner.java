package com.project.kicksdrop.model;

import java.util.HashMap;
import java.util.List;

public class Banner {

//    private String banner_id;
    private String image;
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Banner(String image, String title) {
        this.image = image;
        this.title = title;
    }
    public Banner(){

    }

//    public String getBanner_id() {
//        return banner_id;
//    }
//
//    public void setBanner_id(String banner_id) {
//        this.banner_id = banner_id;
//    }



}
