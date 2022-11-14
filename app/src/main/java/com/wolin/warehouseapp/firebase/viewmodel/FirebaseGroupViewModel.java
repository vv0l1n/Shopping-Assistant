package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
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

    public MutableLiveData<Group> getGroup(String groupId) {
        groupMutableLiveData = new MutableLiveData<>();
        loadGroup(groupId);
        return groupMutableLiveData;
    }

    public MutableLiveData<List<Group>> getGroups(List<String> groupsId) {
        groupMutableLiveData = new MutableLiveData<>();
        loadGroups(groupsId);
        return groupListMutableLiveData;
    }

    private void loadGroups(List<String> groupsId) {
        List<Group> groupList = new ArrayList<>();
        for(String groupId : groupsId) {
            firebaseService.getGroup(groupId, new MyCallback<Group>() {
                @Override
                public void onCallback(Group data) {
                    groupList.add(data);
                }
            });
        }
        groupListMutableLiveData.postValue(groupList);
    }

    public void loadGroup(String groupId) {
        firebaseService.getGroup(groupId, new MyCallback<Group>() {
            @Override
            public void onCallback(Group data) {
                groupMutableLiveData.postValue(data);
            }
        });
    }
}
