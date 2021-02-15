package com.utflnx.whokonws.api.module;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.api.utils.ListObjects;

@Database(entities = {UserModel.class, RoomModel.class}, version = 1)
public abstract class LocalModule extends RoomDatabase {
    private static volatile LocalModule INSTANCE;

    public static LocalModule init(Context context){
        if(INSTANCE == null){
            synchronized (LocalModule.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, LocalModule.class, ListObjects.DATABASE_NAME).build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract LocalService provideRoomService();

}
