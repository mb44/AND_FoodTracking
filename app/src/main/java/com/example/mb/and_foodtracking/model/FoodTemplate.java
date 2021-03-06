package com.example.mb.and_foodtracking.model;

public class FoodTemplate {
    private int id;
    private String name;
    private String duration;
    private int imgResourceId;

    public FoodTemplate(int id, String name, String duration, int imgResourceId) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.imgResourceId = imgResourceId;
    }

    public int getId() {
        return  id;
    }
    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public int getImgResourceId() { return imgResourceId; }
}