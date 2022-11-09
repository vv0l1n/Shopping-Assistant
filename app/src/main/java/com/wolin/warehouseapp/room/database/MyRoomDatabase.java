package com.wolin.warehouseapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wolin.warehouseapp.room.dao.PhotoDao;
import com.wolin.warehouseapp.room.dao.UserDao;
import com.wolin.warehouseapp.utils.model.Photo;
import com.wolin.warehouseapp.utils.model.UserDetails;


@Database(entities = {Photo.class, UserDetails.class} , version = 2)
public abstract class MyRoomDatabase extends RoomDatabase {

    private static MyRoomDatabase mInstance;
    public abstract PhotoDao photoDao();
    public abstract UserDao userDao();

    public static synchronized MyRoomDatabase getmInstance(Context context){

        if (mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext() ,
                            MyRoomDatabase.class
                            ,"PhotoDataBase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}