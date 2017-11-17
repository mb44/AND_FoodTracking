package com.example.mb.and_foodtracking.model;

/**
 * Created by Mb on 17-11-2017.
 */

public class FoodDate {
    private int year;
    private int month;
    private int date;

    public FoodDate() {
        this.year = 0;
        this.month = 0;
        this.date = 0;
    }

    public FoodDate(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}