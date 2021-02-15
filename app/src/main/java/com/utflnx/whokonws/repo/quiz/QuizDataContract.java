package com.utflnx.whokonws.repo.quiz;

import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.List;

public interface QuizDataContract {
    void postQuizRemote(QuizModel quizModel, ActionQuizCallback callback);
    void deleteQuizLocal();
    void getRoomQuizList(RoomModel roomModel, LoadedQuizCallback callback);

    interface ActionQuizCallback{
        void onQuizResponse(QuizModel quizModel);
        void onError(Throwable t);
    }

    interface LoadedQuizCallback{
        void onQuizListLoaded(List<QuizModel> quizModelList);
        void onQuizListEmpty();
        void onError(Throwable t);
    }
}
