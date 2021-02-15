package com.utflnx.whokonws.repo.quiz;

import android.content.Context;

import com.utflnx.whokonws.api.module.LocalModule;
import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizLocal implements QuizDataContract {
    private LocalService localService;
    private ExecutorService executorService;
    private Context mContext;

    public QuizLocal(Context context) {
        mContext = context;
        LocalModule module = LocalModule.init(context);

        localService = module.provideRoomService();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void deleteQuizLocal() {
        executorService.execute(localService::deleteRoom);
    }

    @Override
    public void postQuizRemote(QuizModel quizModel, ActionQuizCallback callback) {}

    @Override
    public void getRoomQuizList(RoomModel roomModel, LoadedQuizCallback callback) {}
}
