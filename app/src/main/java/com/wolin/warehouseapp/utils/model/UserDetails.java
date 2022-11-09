package com.wolin.warehouseapp.utils.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "user_table")
public class UserDetails {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    String Uid;
    String email;
    String name;
    String lastName;
    Long lastProductID;

    public UserDetails(String Uid, String email, String name, String lastName) {
        this.Uid = Uid;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.lastProductID = 0l;
    }


    @NonNull
    public String getUid() {
        return Uid;
    }

    public void setUid(@NonNull String uid) {
        Uid = uid;
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

    public Long getLastProductID() {
        return lastProductID;
    }

    public void setLastProductID(Long lastProductID) {
        this.lastProductID = lastProductID;
    }
}