package com.wisappstudio.hobbing.data;

public class InnerPostData {
    private String number;
    private String image;

    public InnerPostData(String number, String image){
        this.number = number;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }
}
