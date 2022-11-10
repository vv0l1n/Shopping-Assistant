package com.wolin.warehouseapp.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wolin.warehouseapp.room.dao.PhotoDao;
import com.wolin.warehouseapp.room.database.MyRoomDatabase;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Photo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoRoomRepo {

    private PhotoDao photoDao;
    private LiveData<Group> groupLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();


    public LiveData<List<Photo>> getPhotoListLiveData() {
        return photoListLiveData;
    }

    public PhotoRoomRepo(Application application){
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getmInstance(application);
        photoDao = myRoomDatabase.photoDao();
        photoListLiveData = photoDao.getAllPhotos();
    }

    public void insertPhoto(Photo photo){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                photoDao.insertPhoto(photo);
            }
        });
    }

}