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
    private String uid;
    private String email;
    private String name;
    private String lastName;


    public UserDetails(String uid, String email, String name, String lastName) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
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

}