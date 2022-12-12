package com.wolin.warehouseapp.utils.model;


import java.util.List;

public class User {

    private String uid;
    private String email;
    private String name;
    private String lastName;
    private List<String> groups;

    public User() {}
    
    public User(String uid, String email, String name, String lastName, List<String> groups) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.groups = groups;
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

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", groups=" + groups +
                '}';
    }
}