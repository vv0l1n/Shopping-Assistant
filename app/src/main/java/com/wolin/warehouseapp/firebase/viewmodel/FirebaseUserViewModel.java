package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.wolin.warehouseapp.firebase.repo.CallbackListener;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.OnDataUploaded;;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebaseUserViewModel extends ViewModel {
    private FirebaseService firebaseService;
    private FirebaseFirestore firebaseFirestore;
    private UserDetails userDetails;


    public FirebaseUserViewModel(){
        firebaseService = FirebaseService.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void registerUser(UserDetails user){
        firebaseService.registerUser(user);
    }

    public MutableLiveData<UserDetails> getUser(String uid){
        return firebaseService.getUser(uid);
    }

}
