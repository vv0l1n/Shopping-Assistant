package com.wolin.warehouseapp.utils.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Group {
    //id is ownerUid-groupName
    private String groupId;
    private String name;
    //owner is an owner UID
    private String owner;
    //members is a List of members UID
    private List<String> members;
    private List<Product> products;

    public Group(){}

    public Group(String name, String owner) {
        this.groupId = owner + "-" + name;
        this.name = name;
        this.owner = owner;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(owner);
        this.members = temp;
    }

    public String getId() {
        return groupId;
    }

    public void setId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", members=" + members +
                ", products=" + products +
                '}';
    }
}
