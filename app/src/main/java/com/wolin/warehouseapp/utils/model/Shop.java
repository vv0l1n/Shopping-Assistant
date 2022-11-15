package com.wolin.warehouseapp.utils.model;

public class Shop {
    private String name;
    private int shopLogo;

    public Shop(){}

    public Shop(String name, int shopLogo) {
        this.name = name;
        this.shopLogo = shopLogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(int shopLogo) {
        this.shopLogo = shopLogo;
    }
}
