package com.wolin.warehouseapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wolin.warehouseapp.room.dao.PhotoDao;
import com.wolin.warehouseapp.room.dao.UserDao;
import com.wolin.warehouseapp.utils.model.Photo;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;
import com.wolin.warehouseapp.utils.model.UserGroup;


@Database(entities = {Photo.class, UserDetails.class, Product.class, UserGroup.class} , version = 3)
public abstract class MyRoomDatabase extends RoomDatabase {

    private static MyRoomDatabase mInstance;
    public abstract PhotoDao photoDao();
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract UserGroupDao userGroupDao();

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