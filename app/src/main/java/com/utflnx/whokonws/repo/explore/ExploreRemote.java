package com.utflnx.whokonws.repo.explore;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.api.utils.ListObjects;

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
    public void getUsers(LoadedExploreCallback loadedExploreCallback) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_ONLY, new String[]{ListObjects.TABLE_USER}, null, null, null);

        mRemoteService.getAllUsers(APIRequestModel).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body() != null) {
                    if (!response.body().isEmpty()) loadedExploreCallback.onUsersLoaded(response.body());
                    else loadedExploreCallback.onUsersEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                loadedExploreCallback.onError(t);
            }
        });
    }

    @Override
    public void postUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_POST_ONLY, new String[]{ListObjects.TABLE_USER}, userModel, null, null);

        mRemoteService.postUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if (response.body() != null) {
                    actionExploreCallback.onUserResponse(userModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                actionExploreCallback.onError(t);
            }
        });
    }

    @Override
    public void deleteUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_DELETE, new String[]{ListObjects.TABLE_USER}, userModel, null, null);

        mRemoteService.deleteUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if (response.body() != null) {
                    actionExploreCallback.onUserResponse(userModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                actionExploreCallback.onError(t);
            }
        });
    }

    @Override
    public void updateUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_UPDATE_ONLY, new String[]{ListObjects.TABLE_USER}, userModel, null,"userId = "+ userModel.getUserId());

        mRemoteService.updateUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if (response.body() != null){
                    actionExploreCallback.onUserResponse(userModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                actionExploreCallback.onError(t);
            }
        });
    }
}
