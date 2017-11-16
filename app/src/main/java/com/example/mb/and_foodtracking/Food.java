package com.example.mb.and_foodtracking;

/**
 * Created by Mb on 16-11-2017.
 */

public class Food {
    private int id;
    private String name;
    private String storageDate;
    private String expiry;
    private int imgResourceId;

    public Food(int id, String name, String storageDate, String expiry, int imgResourceId) {
        this.id = id;
        this.name = name;
        this.storageDate = storageDate;
        this.expiry = expiry;
        this.imgResourceId = imgResourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }
}
