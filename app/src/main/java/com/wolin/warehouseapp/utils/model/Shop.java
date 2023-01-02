package com.wolin.warehouseapp.utils.model;

import java.io.Serializable;

public class Shop implements Serializable {
    private String name;
    private String shopLogo;

    public Shop(){}

    public Shop(String name, String shopLogo) {
        this.name = name;
        this.shopLogo = shopLogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", shopLogo=" + shopLogo +
                '}';
    }
}
