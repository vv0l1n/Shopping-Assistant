package com.wolin.warehouseapp.room.dao;

import androidx.room.Query;
import androidx.room.Transaction;

import com.wolin.warehouseapp.utils.room.UserInGroups;
import com.wolin.warehouseapp.utils.room.UsersInGroup;

import java.util.List;

public interface UsersGroupsDao {
    @Transaction
    @Query("SELECT * FROM user_table")
    public List<UsersInGroup> getUsersInGroup();

    @Transaction
    @Query("SELECT * FROM group_table")
    public List<UserInGroups> getUserInGroups();
}
