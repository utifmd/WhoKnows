package com.utflnx.whokonws.repo.explore;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ExploreModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.api.utils.ListObjects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreRemote implements ExploreDataContract {
    private final String TAG = getClass().getSimpleName();
    private final RemoteService mRemoteService;

    public ExploreRemote(Context context) {
        mRemoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void getExplores(LoadedExploreCallback loadedExploreCallback) {
        LinkedHashMap<String, String> container = new LinkedHashMap<>();
        container.put("roomId", "null");
        container.put("userId", "null");

        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_JOIN_COUPLE_BY_PARAM,
                new String[]{ListObjects.TABLE_ROOM, ListObjects.TABLE_QUIZ, ListObjects.TABLE_USER},
                container, null, "LIMIT 10"); //"WHERE expired = 1 LIMIT 10"); //"WHERE expired = 0 LIMIT 10"

        mRemoteService.getExplore(requestModel).enqueue(new Callback<List<ExploreModel>>() {
            @Override
            public void onResponse(Call<List<ExploreModel>> call, Response<List<ExploreModel>> response) {
                if (response.body() != null){
                    if(response.body().size() != 0)
                        loadedExploreCallback.onExploreLoaded(response.body());
                    else
                        loadedExploreCallback.onExploreEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<ExploreModel>> call, Throwable t) {
                loadedExploreCallback.onError(t);
            }
        });
    }

    public void getExploresBy(UserModel userModel, RoomModel roomModel, LoadedExploreCallback loadedExploreCallback) {
        LinkedHashMap<String, String> container = new LinkedHashMap<>();
        container.put("roomId", roomModel.getRoomId());
        container.put("userId", userModel.getUserId());

        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_JOIN_BY,
                new String[]{ListObjects.TABLE_ROOM, ListObjects.TABLE_QUIZ, ListObjects.TABLE_USER},
                container, null,null);

        mRemoteService.getExplore(requestModel).enqueue(new Callback<List<ExploreModel>>() {
            @Override
            public void onResponse(Call<List<ExploreModel>> call, Response<List<ExploreModel>> response) {
                if (response.body() != null){
                    if(response.body().size() != 0)
                        loadedExploreCallback.onExploreLoaded(response.body());
                    else
                        loadedExploreCallback.onExploreEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<ExploreModel>> call, Throwable t) {
                loadedExploreCallback.onError(t);
            }
        });
    }
}
