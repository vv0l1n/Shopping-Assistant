package com.wolin.warehouseapp.model;

import com.wolin.warehouseapp.model.UserDetails;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Product {
    private String name;
    private int count;
    private int maxPrice;
    private String note;
    private Shop SHOP;
    private int imageOfProduct;
    private boolean active;
    private SimpleDateFormat date;
    private String owner;
    private String buyer;

    public Product(String name, int count, int maxPrice, String note, Shop SHOP, int imageOfProduct, boolean active, SimpleDateFormat date, String owner) {
        this.name = name;
        this.count = count;
        this.maxPrice = maxPrice;
        this.note = note;
        this.SHOP = SHOP;
        this.imageOfProduct = imageOfProduct;
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

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Shop getSHOP() {
        return SHOP;
    }

    public void setSHOP(Shop SHOP) {
        this.SHOP = SHOP;
    }

    public int getImageOfProduct() {
        return imageOfProduct;
    }

    public void setImageOfProduct(int imageOfProduct) {
        this.imageOfProduct = imageOfProduct;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SimpleDateFormat getDate() {
        return date;
    }

    public void setDate(SimpleDateFormat date) {
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

    private enum Shop {
        LEWIATAN, BIEDRONKA, LIDL, CARREFOUR, KAUFLAND, ZABKA, DINO, DELIKATESY_CENTRUM, TOP_MARKET, NONE;
    }
}
