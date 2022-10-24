package com.wolin.warehouseapp;

public class MainMenuItem {
    private String name;
    private int count;
    private String category;
    private String zone;
    private String rack;
    private String shelf;
    private int image;

    public MainMenuItem(String name, int count, String category, String zone, String rack, String shelf, int image) {
        this.name = name;
        this.count = count;
        this.category = category;
        this.zone = zone;
        this.rack = rack;
        this.shelf = shelf;
        this.image = image;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
