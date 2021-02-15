package com.utflnx.whokonws.repo.quiz;

import android.content.Context;

import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;

public class QuizRepository implements QuizDataContract {
    private QuizDataContract quizRemote, quizLocal;
    private Context mContext;

    public QuizRepository(Context context) {
        mContext = context;
        quizRemote = new QuizRemote(context);
        quizLocal = new QuizLocal(context);
    }

    @Override
    public void postQuizRemote(QuizModel quizModel, ActionQuizCallback callback) {
        quizRemote.postQuizRemote(quizModel, callback);
    }

    @Override
    public void deleteQuizLocal() {
        quizLocal.deleteQuizLocal();
    }

    @Override
    public void getRoomQuizList(RoomModel roomModel, LoadedQuizCallback callback) {
        quizRemote.getRoomQuizList(roomModel, callback);
    }
}
