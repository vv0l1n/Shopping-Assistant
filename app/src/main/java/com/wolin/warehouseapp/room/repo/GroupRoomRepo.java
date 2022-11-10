package com.wolin.warehouseapp.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wolin.warehouseapp.room.dao.GroupDao;
import com.wolin.warehouseapp.room.database.MyRoomDatabase;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Photo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GroupRoomRepo {

    private GroupDao groupDao;
    private LiveData<Group> groupListLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public GroupRoomRepo(Application application, String groupId){
        MyRoomDatabase myRoomDatabase = MyRoomDatabase.getmInstance(application);
        groupDao = myRoomDatabase.groupDao();
        groupListLiveData = groupDao.getGroup(groupId);
    }

    public void insertGroup(Group group){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                groupDao.insertGroup(group);
            }
        });
    }
}
