package com.wolin.warehouseapp.utils.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.List;

public class UsersInGroup {
    @Embedded private Group group;
    @Relation(
            parentColumn = "groupId",
            entityColumn = "uid",
            associateBy = @Junction(UsersGroups.class))
    private List<UserDetails> users;
}
