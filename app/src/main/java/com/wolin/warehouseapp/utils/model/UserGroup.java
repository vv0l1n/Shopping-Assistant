package com.wolin.warehouseapp.utils.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "group_table")
public class UserGroup {
    //id is ownerUid/groupName
    @PrimaryKey(autoGenerate = false)
    private String id;
    private String name;
    //owner is an owner UID
    private String owner;
    //members is a List of members UID
    private List<String> members;

    public UserGroup(String name, String owner) {
        this.id = owner + "/" + name;
        this.name = name;
        this.owner = owner;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(owner);
        this.members = temp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
