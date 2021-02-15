package com.utflnx.whokonws.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.utflnx.whokonws.api.utils.ListObjects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = ListObjects.TABLE_CURRENT_ROOM)
public class RoomModel implements Serializable {

    @NonNull @PrimaryKey()
    private String roomId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "expired")
    private int expired;

    @ColumnInfo(name = "minute")
    private String minute;

    public RoomModel(){}
    public RoomModel(@NotNull String roomId){
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired == 1;
    }

    public void setExpired(boolean expired) {
        if (expired) this.expired = 1;
        else  this.expired = 0;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}