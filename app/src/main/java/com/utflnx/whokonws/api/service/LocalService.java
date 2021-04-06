package com.utflnx.whokonws.api.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

@Dao
public interface LocalService { // @Query("SELECT * FROM _table_users") // List<User> getAllUsers();

    @Query("SELECT * FROM _table_current_user LIMIT 1") // @Query("SELECT * FROM _table_users WHERE userId = :userId")
    UserModel getCurrentUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(UserModel userModel);

    @Query("DELETE FROM _table_current_user")
    void deleteUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCurrentRoom(RoomModel roomModel);

    @Query("SELECT * FROM _table_current_room LIMIT 1")
    RoomModel getCurrentRoom();

    @Query("DELETE FROM _table_current_room")
    void deleteRoom();
}
