package com.wolin.warehouseapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wolin.warehouseapp.room.dao.PhotoDao;
import com.wolin.warehouseapp.utils.model.Photo;


@Database(entities = {Photo.class} , version = 1)
public abstract class PhotoDatabase extends RoomDatabase {

    private static PhotoDatabase mInstance;
    public abstract PhotoDao photoDao();

    public static synchronized  PhotoDatabase getmInstance(Context context){

        if (mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext() ,
                            PhotoDatabase.class
                            ,"PhotoDataBase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}