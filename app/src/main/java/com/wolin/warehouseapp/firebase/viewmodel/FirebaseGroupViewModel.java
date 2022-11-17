package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.utils.model.Group;

import java.util.List;

public class FirebaseGroupViewModel  extends ViewModel {

    private FirebaseService firebaseService;
    private LiveData<List<Group>> groupListLiveData;
    private MutableLiveData<Group> groupMutableLiveData;

    public FirebaseGroupViewModel() {
        firebaseService = FirebaseService.getInstance();
        groupMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Group>> getGroups(String uid) {
        if(groupListLiveData == null) {
            groupListLiveData = new MutableLiveData<List<Group>>();
            loadGroups(uid);
        }
        return groupListLiveData;
    }

    private void loadGroups(String uid) {
        System.out.println("LOADGROUPS 111111111111111");
            groupListLiveData = Transformations.switchMap(firebaseService.getGroups(uid), groups -> {
                System.out.println("LOADGROUPS: ");
                for(Group group : groups) {
                    System.out.println(group.getName() + ":::" + group.getProducts());
                }
                MutableLiveData<List<Group>> liveData = new MutableLiveData<>();
                liveData.postValue(groups);
                return liveData;
            });
        }
}
