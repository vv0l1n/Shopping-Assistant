package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class FirebaseGroupViewModel {

    private FirebaseService firebaseService;
    private MutableLiveData<List<Group>> groupListMutableLiveData;
    private MutableLiveData<Group> groupMutableLiveData;

    public FirebaseGroupViewModel(){
        firebaseService = FirebaseService.getInstance();
        groupListMutableLiveData = new MutableLiveData<>();
        groupMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Group> getGroupMutableLiveData(String groupId) {
        return firebaseService.getGroupMutableLiveData(groupId);
    }
}
