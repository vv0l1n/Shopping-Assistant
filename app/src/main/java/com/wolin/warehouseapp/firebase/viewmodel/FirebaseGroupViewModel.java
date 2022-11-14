package com.wolin.warehouseapp.firebase.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public LiveData<Group> getGroup(String groupId) {
        loadGroup(groupId);
        return groupMutableLiveData;
    }

    public LiveData<List<Group>> getGroups(String uid) {
        loadGroups(uid);
        System.out.println("GRUPA: " + groupListMutableLiveData.getValue());
        return groupListMutableLiveData;
    }

    private void loadGroups(String uid) {
            firebaseService.getGroups(uid, new MyCallback<Group>() {
                @Override
                public void onCallback(Group group) {
                    List<Group> tempList = new ArrayList<>();
                    tempList.add(group);
                    groupListMutableLiveData.postValue(tempList);
                }
            });
        }

    public void loadGroup(String groupId) {
        firebaseService.getGroup(groupId, new MyCallback<Group>() {
            @Override
            public void onCallback(Group data) {
                groupMutableLiveData.postValue(data);
                System.out.println("DANE ZALADOWANE: " + data);
            }
        });
    }
}
