package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirebaseUserViewModel extends ViewModel {
    private FirebaseService firebaseService;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<User> user;
    private volatile MutableLiveData<List<User>> userLiveDataList;


    public FirebaseUserViewModel(){
        firebaseService = FirebaseService.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void registerUser(User user){
        firebaseService.registerUser(user);
    }

    public LiveData<User> getUser(String uid){
        if(user == null) {
            user = new MutableLiveData<User>();
            loadUser(uid);
        }
        return user;
    }

    private void loadUser(String uid) {
        firebaseService.getUser(uid, new MyCallback<User>() {
            @Override
            public void onCallback(User data) {
                user.postValue(data);
            }
        });
    }

    public LiveData<List<User>> getUsers(List<String> members) {
        if(userLiveDataList == null) {
            userLiveDataList = new MutableLiveData<List<User>>();
            userLiveDataList.postValue(new ArrayList<>());
        }
        loadUsers(members);
        return userLiveDataList;
    }

    private void loadUsers(List<String> members) {
        for(String user : members) {
            firebaseService.getUser(user, new MyCallback<User>() {
                @Override
                public void onCallback(User data) {

                    List<User> tempList = userLiveDataList.getValue();
                    boolean found = false;
                    for(int i = 0; i < tempList.size(); i++) {
                        if(tempList.get(i).getUid().equals(data.getUid())) {
                            tempList.set(i, data);
                            found = true;
                        }
                    }
                    if(!found) {
                        tempList.add(data);
                    }
                    userLiveDataList.postValue(tempList);
                }
            });
        }

    }
}
