package com.wolin.warehouseapp.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wolin.warehouseapp.room.repo.UserRoomRepo;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.List;

public class UserRoomViewModel extends AndroidViewModel {

    private UserRoomRepo userRoomRepo;

    public UserRoomViewModel(@NonNull Application application) {
        super(application);

        userRoomRepo = new UserRoomRepo(application);
    }

    public void insertUser(UserDetails userDetails) {userRoomRepo.insertUser(userDetails);}

    public LiveData<UserDetails> getUser(String uid) {
        return userRoomRepo.getCurrentUserLiveData(uid);
    }

    public LiveData<List<UserDetails>> getAllPhotosLiveData(){
        return userRoomRepo.getUserListLiveData();
    }
}
