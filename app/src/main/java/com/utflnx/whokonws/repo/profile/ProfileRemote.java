package com.utflnx.whokonws.repo.profile;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRemote implements ProfileDataContract {
    private final String TAG = getClass().getSimpleName();
    RemoteService mRemoteService;

    public ProfileRemote(Context context) {
        mRemoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void getRemoteProfile(UserModel userModel, LoadedProfileCallBack callBack) {
        HashMap<String, String> customContainer = new HashMap<>();
        customContainer.put(userModel.getEmail(), userModel.getPassword());

        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_SIGN_IN, new String[]{ListObjects.TABLE_USER}, customContainer, null);

        mRemoteService.getUser(APIRequestModel).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body() != null) {
                    if (response.body().size() != 0) callBack.onProfileLoaded(response.body().get(0));
                    else callBack.onError(new Throwable("Please check your email end password.")); // callBack.onProfileEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void postRemoteProfile(UserModel userModel, ActionProfileCallBack callBack) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_POST_ONLY, new String[]{ListObjects.TABLE_USER}, userModel, null);

        mRemoteService.postUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if (response.body() != null) {
                    callBack.onProfileResponse(userModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void updateUser(UserModel currentUserModel, ActionProfileCallBack callBack) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_UPDATE_ONLY, new String[]{ListObjects.TABLE_USER}, currentUserModel, "userId = "+ currentUserModel.getUserId());

        mRemoteService.updateUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if(response.body() != null) callBack.onProfileResponse(currentUserModel);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void deleteRemoteUser(UserModel currentUserModel, ActionProfileCallBack callBack) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_DELETE, new String[]{ListObjects.TABLE_USER}, currentUserModel,"userId = "+ currentUserModel.getUserId());

        mRemoteService.updateUser(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.message());
                if (response.body() != null){
                    callBack.onProfileResponse(currentUserModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }


    @Override
    public void getLocalProfile(LoadedProfileCallBack callBack) {
    }

    @Override
    public void postLocalProfile(UserModel userModel) {
    }

    @Override
    public void deleteLocalUser() {}
}
