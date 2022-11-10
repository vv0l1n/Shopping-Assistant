package com.wolin.warehouseapp.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wolin.warehouseapp.utils.model.Group;

@Dao
public interface GroupDao {

    @Insert
    void insertGroup(Group group);

    @Query("SELECT * FROM  group_table WHERE groupId = :groupId")
    LiveData<Group> getGroup(String groupId);
}
