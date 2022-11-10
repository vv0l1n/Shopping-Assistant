package com.wolin.warehouseapp.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserDetails user);

    @Query("SELECT * FROM  user_table WHERE uid = :uid")
    UserDetails getUser(String uid);
}