package com.example.mb.and_foodtracking;

/**
 * Created by Mb on 07-11-2017.
 */

public class FoodTemplate {
    private String name;
    private String duration;
    private int imgResourceId;

    public FoodTemplate(String name, String duration, int imgResourceId) {
        this.name = name;
        this.duration = duration;
        this.imgResourceId = imgResourceId;
    }

    public String getName() {
        return name;
    }

    public String getRegDate() {
        return duration;
    }

    public int getImgResourceId() { return imgResourceId; }
}
