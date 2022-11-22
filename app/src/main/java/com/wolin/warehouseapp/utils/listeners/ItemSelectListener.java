package com.wolin.warehouseapp.utils.listeners;


import com.wolin.warehouseapp.utils.model.Shop;

public interface ItemSelectListener<T> {
    void onItemClick(T item);
}
