package com.wolin.warehouseapp.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.wolin.warehouseapp.utils.model.Product;

public interface ProductsInGroup {

    @Insert
    void insertProduct(Product product);

    @Query("SELECT * FROM  product_table where productId = :productId")
    LiveData<Product> getProductById(String productId);
}
