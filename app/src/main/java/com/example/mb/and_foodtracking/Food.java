package com.example.mb.and_foodtracking;

/**
 * Created by Mb on 07-11-2017.
 */

public class Food {
    private String name;
    private String regDate;
    private String expDate;
    
    public Food(String name, String regDate, String expDate) {
        this.name = name;
        this.regDate = regDate;
        this.expDate = expDate;
    }

    public String getName() {
        return name;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getExpDate() {
        return expDate;
    }
}
