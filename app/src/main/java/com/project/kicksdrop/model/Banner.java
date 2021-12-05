package com.project.kicksdrop.model;

public class Banner {

    private String banner_id;
    private String banner_images;
    private String banner_title;

    public String getBannerimages() {
        return banner_images;
    }

    public void  setBannerimages(String Images){
        this.banner_images = banner_images;
    }

    public String getBannertitle() {
        return banner_title;
    }

    public void setBannertitle(String Title) {
        this.banner_title = banner_title;
    }

}
