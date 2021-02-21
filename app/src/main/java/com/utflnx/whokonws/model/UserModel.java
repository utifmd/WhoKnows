package com.utflnx.whokonws.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.utflnx.whokonws.api.utils.ListObjects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = ListObjects.TABLE_CURRENT_USER)
public class UserModel implements Serializable {

    @PrimaryKey()
    @NonNull()
    private String userId = "";

    @ColumnInfo(name = "fullName")
    private String fullName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "password")
    private String password;

    public UserModel(){}
    public UserModel(@NotNull String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    @ColumnInfo(name = "ridOwned") // @Embedded // ganti struktur pojo to sql language able!
//    private String ridOwned;
//    public void setRidOwnedArray(String... vararg){
//        this.ridOwned = Arrays.toString(vararg);
//    }
//    public String[] getRidOwnedArray(){
//        return ridOwned.split(",");
//    }
}