package com.wolin.warehouseapp.utils.room;

import androidx.room.Entity;

@Entity(primaryKeys = {"UserID", "GroupID"})
public class UsersGroups {
    private String UserID;
    private String GroupID;
}
