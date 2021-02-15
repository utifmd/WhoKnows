package com.utflnx.whokonws.ui.quiz;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.List;

public interface QuizMainContract {
    interface Presenter extends BasePresenter{
        void createOwnerQuiz(QuizModel quizModel);
        void getRoomQuizList(RoomModel roomModel);
        void finishTakenQuiz(ParticipantModel participantModel);
    }

    interface View extends BaseView<Presenter>{
        void onRoomQuizListLoaded(List<QuizModel> quizModelList);
        void onQuizDisplay();
        void onQuizEmpty();
        void onOwnerQuizRemoteSaved(QuizModel quizModel);
        void onFinishParticipantTakenQuiz(ParticipantModel participantModel);
        void onError(Throwable t);
        void onProgressShow();
        void onProgressHide();
    }
}
