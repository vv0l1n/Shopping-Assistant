package com.wolin.warehouseapp.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wolin.warehouseapp.room.dao.UserDao;
import com.wolin.warehouseapp.room.database.MyRoomDatabase;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRoomRepo {

    private UserDao userDao;
    private LiveData<UserDetails> userLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();


    public LiveData<List<UserDetails>> getUserListLiveData() {
        return userLiveData;
    }

    public UserRoomRepo(Application application){
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getmInstance(application);
        userDao = myRoomDatabase.userDao();
        userLiveData = userDao.getUser(String uid);
    }

    public void insertUser(UserDetails user){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insertUser(user);
            }
        });
    }

    public LiveData<UserDetails> getCurrentUserLiveData(String uid){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.getCurrentUser(uid);
            }
        });
        return null;
    }

}
