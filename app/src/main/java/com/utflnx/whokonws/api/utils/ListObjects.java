package com.utflnx.whokonws.api.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.utflnx.whokonws.R;

public class ListObjects {
    public static final String DATABASE_NAME = "_who_knows";
    public static final String TABLE_PARTICIPANT = "_table_participant";
    public static final String TABLE_QUIZ = "_table_quiz";
    public static final String TABLE_ROOM = "_table_room";
    public static final String TABLE_USER = "_table_user";
    public static final String TABLE_RESULT = "_table_result";
    public static final String TABLE_CURRENT_USER = "_table_current_user";
    public static final String TABLE_CURRENT_ROOM = "_table_current_room";
    public static final String BASE_URL = "http://10.0.2.2:8888/www/"; // http://192.168.1.38:8888/www/"; //"http://localhost:8888/www/";
    public static final String PARAM_WHO_KNOWS_URL = "who_knows.php/";
    public static final String ABOUT_FETCH_ONLY = "fetch_only";
    public static final String ABOUT_FETCH_SINGLE = "fetch_single";
    public static final String ABOUT_FETCH_COUPLE_BY = "fetch_couple_by";
    public static final String ABOUT_FETCH_JOIN = "fetch_join";
    public static final String ABOUT_FETCH_JOIN_BY = "fetch_join_by";
    public static final String ABOUT_FETCH_SIGN_IN = "fetch_sign_in";
    public static final String ABOUT_POST_ONLY = "post_only";
    public static final String ABOUT_DELETE = "delete";
    public static final String ABOUT_DELETE_COUPLE = "delete_couple";
    public static final String ABOUT_UPDATE_POST_COUPLE = "update_post_couple";
    public static final String ABOUT_DELETE_TRIPLE = "delete_triple";
    public static final String ABOUT_DELETE_QUAD = "delete_quad";
    public static final String ABOUT_UPDATE_ONLY = "update_only";
    public static final int KEY_CREATE_ROOM = 212121;
    public static final int KEY_TAKE_ROOM = 313131;
    public static final String KEY_CURRENT_USER = "keyProfileCurrentUser";


    public static SharedPreferences provideSharedPrefs(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static FragmentTransaction navigateTo(Context context, Fragment clazz, boolean isAddBackStack){
        FragmentTransaction transaction = ((AppCompatActivity)context)
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootMainFrame, clazz);

        if (isAddBackStack){
            transaction.addToBackStack(clazz.toString());
        }

        return transaction;
    }
    public static FragmentManager fragmentManager(Context context){
        return ((AppCompatActivity)context).getSupportFragmentManager();
    }

    public static void visibleGoneView(View[] visible, View... gone){
        for (View view: gone ){
            view.setVisibility(View.GONE);
        }

        for (View view : visible) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
