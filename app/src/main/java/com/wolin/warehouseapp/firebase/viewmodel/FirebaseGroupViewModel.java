package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;
import java.util.Map;

public class FirebaseGroupViewModel  extends ViewModel {

    private FirebaseService firebaseService;
    private MutableLiveData<List<Group>> groupListMutableLiveData;
    private MutableLiveData<Group> groupMutableLiveData;

    public FirebaseGroupViewModel() {
        firebaseService = FirebaseService.getInstance();
        groupListMutableLiveData = new MutableLiveData<>();
        groupMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Group> getGroupMutableLiveData(String groupId) {
        return firebaseService.getGroup(groupId);
    }

    public MutableLiveData<Map<String, String>> getUserGroups(String uid) {
        return firebaseService.getUserGroupsName(uid);
    }
}
