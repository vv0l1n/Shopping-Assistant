package com.wolin.warehouseapp.utils.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetails {

    private String uid;
    private String email;
    private String name;
    private String lastName;
    private List<String> groups;


    public UserDetails(String uid, String email, String name, String lastName, List<String> groups) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.groups = groups;
    }


    public static UserDetails toUser(DocumentSnapshot documentSnapshot) {
        try {
            String uid = documentSnapshot.getId();

            Map<String, Object> data = documentSnapshot.getData();
            String email = (String) data.get("email");
            String name = (String) data.get("name");
            String lastName = (String) data.get("lastName");
            List<String> groups = (List<String>) data.get("groups");

            UserDetails user = new UserDetails(uid, email, name, lastName, groups);
            return user;

        } catch (Exception exc) {
            System.out.println(exc);
        }
        return null;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}