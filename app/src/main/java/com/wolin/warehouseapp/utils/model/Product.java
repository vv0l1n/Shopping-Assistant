package com.wolin.warehouseapp.utils.model;


import android.net.Uri;

import java.net.URL;

public class Product {


    private Long id;
    private String name;
    private int count;
    private double maxPrice;
    private String note;
    private com.wolin.warehouseapp.utils.model.Shop shop;
    private Uri photo;
    private boolean active;
    private String date;
    private String owner;
    private String buyer;
    private String url;

    public Product(String name, int count, double maxPrice, String note, Shop shop, Uri photo, boolean active, String date, String owner) {
        this.name = name;
        this.count = count;
        this.maxPrice = maxPrice;
        this.note = note;
        this.shop = shop;
        this.photo = photo;
        this.active = active;
        this.date = date;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop SHOP) {
        this.shop = SHOP;
    }

    public Uri getUri() {
        return photo;
    }

    public void setUri(Uri imageOfProduct) {
        this.photo = imageOfProduct;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
