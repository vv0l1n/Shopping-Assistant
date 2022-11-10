package com.wolin.warehouseapp.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

@Dao
public interface ProductDao {

    @Insert
    void insertUser(Product product);
}
