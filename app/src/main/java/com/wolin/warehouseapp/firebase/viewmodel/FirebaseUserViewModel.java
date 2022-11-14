package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.firebase.repo.OnDataUploaded;;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebaseUserViewModel extends ViewModel implements Observer<UserDetails> {
    private FirebaseService firebaseService;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<UserDetails> user;


    public FirebaseUserViewModel(){
        firebaseService = FirebaseService.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void registerUser(UserDetails user){
        firebaseService.registerUser(user);
    }

    public MutableLiveData<UserDetails> getUser(String uid){
        user = new MutableLiveData<>();
        loadUser(uid);
        return user;
    }

    private void loadUser(String uid) {
        firebaseService.getUser(uid, new MyCallback<MutableLiveData<UserDetails>>() {
            @Override
            public void onCallback(MutableLiveData<UserDetails> data) {
                user = data;
            }
        });
    }

    @Override
    public void onChanged(UserDetails userDetails) {

    }
}
