package com.wolin.warehouseapp;

import java.util.List;

public class UserGroup {
    private Long id;
    private String name;
    private UserDetails owner;
    private List<UserDetails> members;

    public UserGroup(Long id, String name, UserDetails owner) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDetails getOwner() {
        return owner;
    }

    public void setOwner(UserDetails owner) {
        this.owner = owner;
    }

    public List<UserDetails> getMembers() {
        return members;
    }

    public void setMembers(List<UserDetails> members) {
        this.members = members;
    }
}
