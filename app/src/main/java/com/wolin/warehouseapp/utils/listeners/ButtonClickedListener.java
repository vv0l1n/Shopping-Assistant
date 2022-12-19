package com.wolin.warehouseapp.utils.listeners;

import com.wolin.warehouseapp.utils.model.GroupInvite;

public interface ButtonClickedListener {
    void clicked(GroupInvite invite, String buttonName);
}
