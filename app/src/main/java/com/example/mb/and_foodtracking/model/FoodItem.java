package com.example.mb.and_foodtracking.model;

import android.support.annotation.NonNull;

public class FoodItem {
    private String tagid;
    private int foodid;
    private String name;
    private FoodDate registry;
    private FoodDate expiry;
    private int imgResourceId;

    public FoodItem() {
        this.foodid = 0;
        this.name = "";
        this.registry = new FoodDate();
        this.expiry = new FoodDate();
        this.imgResourceId = -1;
    }

    // This contructor decides what gets serialized/deserialized
    public FoodItem(int foodid, FoodDate registry, FoodDate expiry) {
        this.foodid = foodid;
        this.registry = registry;
        this.expiry = expiry;
    }

    public String getTagId() {
        return tagid;
    }

    public void setTagId(String tagid) {
        this.tagid = tagid;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FoodDate getRegistry() {
        return registry;
    }

    public FoodDate getExpiry() {
        return expiry;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

}
