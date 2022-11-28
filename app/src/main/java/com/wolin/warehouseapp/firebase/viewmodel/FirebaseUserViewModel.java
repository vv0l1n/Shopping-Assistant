package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebaseUserViewModel extends ViewModel {
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

    public LiveData<UserDetails> getUser(String uid){
        System.out.println("Pobieram uzytkownika");
        if(user == null) {
            user = new MutableLiveData<UserDetails>();
            loadUser(uid);
        }
        return user;
    }

    private void loadUser(String uid) {
        System.out.println("loadUser");
        firebaseService.getUser(uid, new MyCallback<UserDetails>() {
            @Override
            public void onCallback(UserDetails data) {
                user.postValue(data);
                System.out.println("grupy uzytkownika: " + data.getGroups());
            }
        });
    }
}
