package com.example.mb.and_foodtracking.model;

public class FoodItem {
    private int tagid;
    private int foodid;
    private String name;
    private FoodDate registry;
    private FoodDate expiry;
    private int imgResourceId;

    public FoodItem() {
        this.tagid = 0;
        this.foodid = 0;
        this.name = "";
        this.registry = new FoodDate();
        this.expiry = new FoodDate();
        this.imgResourceId = -1;
    }

    public FoodItem(int tagid, int foodid, FoodDate registry, FoodDate expiry) {
        this.tagid = tagid;
        this.foodid = foodid;
        this.name = "";
        this.registry = registry;
        this.expiry = expiry;
        this.imgResourceId = -1;
    }

    public FoodItem(int id, String name, FoodDate registry, FoodDate expiry, int imgResourceId) {
        this.tagid = id;
        foodid = 0;
        this.name = name;
        this.registry = registry;
        this.expiry = expiry;
        this.imgResourceId = imgResourceId;
    }

    public int getTagid() {
        return tagid;
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
