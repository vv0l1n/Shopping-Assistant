package com.wolin.warehouseapp.utils.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.List;

public class UserInGroups {
    @Embedded
    private UserDetails user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "groupId",
            associateBy = @Junction(UsersGroups.class))
    private List<Group> groups;
}
