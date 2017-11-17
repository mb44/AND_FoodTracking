package com.example.mb.and_foodtracking.model;

public class FoodType {
    private int foodid;
    private String name;

    public FoodType() {
    }

    public FoodType(int foodid, String name) {
        this.foodid = foodid;
        this.name = name;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
