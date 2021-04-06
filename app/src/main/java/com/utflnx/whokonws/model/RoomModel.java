package com.utflnx.whokonws.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.utflnx.whokonws.api.utils.ListObjects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = ListObjects.TABLE_CURRENT_ROOM)
public class RoomModel extends UserModel implements Serializable {

    @ColumnInfo(name = "roomId")
    private String roomId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "expired")
    private String expired;

    @ColumnInfo(name = "minute")
    private String minute;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired.equals("1");
    }

    public void setExpire(boolean expired) {
        if (expired) this.expired = "1";
        else  this.expired = "0";
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}