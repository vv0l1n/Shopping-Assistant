package com.wolin.warehouseapp.utils.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

@Entity(tableName = "user_table")
public class UserDetails {

    @PrimaryKey(autoGenerate = false)
    String email;
    String name;
    String lastName;
    List<Product> products;
    Long lastProductID;

    public UserDetails(String email, String name, String lastName) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.lastProductID = 0l;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<com.wolin.warehouseapp.utils.model.Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Long getLastProductID() {
        return lastProductID;
    }

    public void setLastProductID(Long lastProductID) {
        this.lastProductID = lastProductID;
    }
}