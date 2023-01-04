package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.GroupInvite;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGroupViewModel  extends ViewModel {

    private FirebaseService firebaseService;
    private LiveData<List<Group>> groupListLiveData;
    private MutableLiveData<Group> groupLiveData;

    public FirebaseGroupViewModel() {
        firebaseService = FirebaseService.getInstance();
    }

    public LiveData<List<Group>> getGroups(String uid) {
        if (groupListLiveData == null) {
            groupListLiveData = new MutableLiveData<List<Group>>();
            loadGroups(uid);
        }
        return groupListLiveData;
    }

    private void loadGroups(String uid) {
        groupListLiveData = Transformations.switchMap(firebaseService.getGroups(uid), groups -> {
            MutableLiveData<List<Group>> liveData = new MutableLiveData<>();
            liveData.postValue(groups);
            if (liveData != null) {
                for (Group group : groups) {
                    System.out.println(group);
                }
            }
            return liveData;
        });
    }


    public void addGroup(String uid, String name, List<String> groups) {
        firebaseService.addGroup(new Group(name, uid), groups);
    }

    public void kickMember(String uid, String groupId) {
        firebaseService.deleteFromGroup(uid, groupId);
        firebaseService.deleteUserProductsInGroup(uid, groupId);
    }

    public LiveData<Group> getGroup(String groupId) {
        if (groupLiveData == null) {
            groupLiveData = new MutableLiveData<Group>();
            loadGroup(groupId);
        }
        return groupLiveData;
    }

    private void loadGroup(String groupId) {
        firebaseService.getSimpleGroup(groupId, new MyCallback<Group>() {
            @Override
            public void onCallback(Group data) {
                groupLiveData.postValue(data);
            }
        });
    }

    public void leave(String groupId, String uid) {
        firebaseService.deleteFromGroup(uid, groupId);
    }

    public void delete(String groupId) {
        firebaseService.deleteGroup(groupId);
    }
}
