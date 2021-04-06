package com.utflnx.whokonws.repo.result;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultRemote implements ResultDataContract {
    private final String TAG = getClass().getSimpleName();
    RemoteService remoteService;

    public ResultRemote(Context context) {
        remoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void getResultBy(RoomModel roomModel, LoadedResultCallback resultCallback) {
        HashMap<String, String> container = new HashMap<>();
        container.put("roomId", roomModel.getRoomId());

        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_SINGLE, new String[]{ListObjects.TABLE_RESULT}, container, null, null);

        remoteService.getResultBy(requestModel).enqueue(new Callback<List<ResultModel>>() {
            @Override
            public void onResponse(Call<List<ResultModel>> call, Response<List<ResultModel>> response) {
                if (response.body() != null){
                    if (response.body().size() != 0)
                        resultCallback.onResultLoaded(response.body());
                    else
                        resultCallback.onResultEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<ResultModel>> call, Throwable t) {
                resultCallback.onError(t);
            }
        });
    }
}
