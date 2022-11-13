package com.wolin.warehouseapp.firebase.repo;

import androidx.lifecycle.MutableLiveData;

import com.wolin.warehouseapp.utils.model.UserDetails;

public interface UserCallback {
    void onCallback(MutableLiveData<UserDetails> mutableLiveDataUser);
}
