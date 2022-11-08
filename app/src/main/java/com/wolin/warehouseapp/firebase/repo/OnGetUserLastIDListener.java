package com.wolin.warehouseapp.firebase.repo;

import com.wolin.warehouseapp.utils.model.UserDetails;

public interface OnGetUserLastIDListener {
    void onGetUserLastID(UserDetails userDetails);
    void onError(Exception taskException);
}
