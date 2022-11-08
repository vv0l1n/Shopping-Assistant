package com.wolin.warehouseapp.room.repo;

import android.app.Application;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LiveData;

import com.wolin.warehouseapp.room.dao.PhotoDao;
import com.wolin.warehouseapp.room.database.PhotoDatabase;
import com.wolin.warehouseapp.utils.model.Photo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoRoomRepo {

    private PhotoDao photoDao;
    private LiveData<List<Photo>> photoListLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();


    public LiveData<List<Photo>> getPhotoListLiveData() {
        return photoListLiveData;
    }

    public PhotoRoomRepo(Application application){
        PhotoDatabase photoDatabase = PhotoDatabase.getmInstance(application);
        photoDao = photoDatabase.photoDao();
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