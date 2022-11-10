package com.wolin.warehouseapp.utils.room;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class ProductsInGroup {
    @Embedded private Group group;
    @Relation(
            parentColumn = "groupId",
            entityColumn = "productId"
    )
    private List<Product> products;
}
