package com.utflnx.whokonws.repo.quiz;

import android.content.Context;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRemote implements QuizDataContract {
    private final String TAG = getClass().getSimpleName();
    private RemoteService remoteService;
    private Context mContext;

    public QuizRemote(Context context) {
        mContext = context;
        remoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void postQuizRemote(QuizModel quizModel, ActionQuizCallback callback) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_POST_ONLY, new String[]{ListObjects.TABLE_QUIZ}, quizModel, null);

        remoteService.postQuizRoom(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) callback.onQuizResponse(quizModel);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void deleteQuizLocal() {}

    @Override
    public void getRoomQuizList(RoomModel roomModel, LoadedQuizCallback callback) {
        HashMap<String, String> container = new HashMap<>();
        container.put("roomId", roomModel.getRoomId());

        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(ListObjects.ABOUT_FETCH_JOIN_BY, new String[]{ListObjects.TABLE_QUIZ, ListObjects.TABLE_ROOM}, container, null);

        remoteService.getRoomQuizList(APIRequestModel).enqueue(new Callback<List<QuizModel>>() {
            @Override
            public void onResponse(Call<List<QuizModel>> call, Response<List<QuizModel>> response) {
                if (response.body() != null){
                    if (response.body().size() != 0) callback.onQuizListLoaded(response.body());
                    else callback.onQuizListEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<QuizModel>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
