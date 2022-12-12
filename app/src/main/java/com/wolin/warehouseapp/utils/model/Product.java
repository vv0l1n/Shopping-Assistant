package com.wolin.warehouseapp.utils.model;

public class Product {

    private String productId;
    private String name;
    private int count;
    private double maxPrice;
    private String note;
    private Shop shop;
    private String photo;
    private boolean active;
    private String date;
    private String dateToBuy;
    private String priority;
    private String owner;
    private String buyer;

    public Product(){}

    public Product(String name, int count, double maxPrice, String note, Shop shop, boolean active, String date, String dateToBuy, String priority, String owner) {
        this.productId = owner + System.currentTimeMillis();
        this.name = name;
        this.count = count;
        this.maxPrice = maxPrice;
        this.note = note;
        this.shop = shop;
        this.active = active;
        this.date = date;
        this.dateToBuy = dateToBuy;
        this.priority = priority;
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

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String imageOfProduct) {
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String id) {
        this.productId = id;
    }

    public String getDateToBuy() {
        return dateToBuy;
    }

    public void setDateToBuy(String dateToBuy) {
        this.dateToBuy = dateToBuy;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", maxPrice=" + maxPrice +
                ", note='" + note + '\'' +
                ", shop=" + shop +
                ", photo='" + photo + '\'' +
                ", active=" + active +
                ", date='" + date + '\'' +
                ", dateToBuy='" + dateToBuy + '\'' +
                ", priority='" + priority + '\'' +
                ", owner='" + owner + '\'' +
                ", buyer='" + buyer + '\'' +
                '}';
    }
}
