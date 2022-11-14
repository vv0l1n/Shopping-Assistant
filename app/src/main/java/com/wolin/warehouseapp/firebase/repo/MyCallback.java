package com.wolin.warehouseapp.firebase.repo;

import androidx.lifecycle.MutableLiveData;

import com.wolin.warehouseapp.utils.model.UserDetails;

public interface MyCallback<T> {
    void onCallback(T data);
}
