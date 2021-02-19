package com.utflnx.whokonws.ui.quiz;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;

import java.util.List;

public interface QuizMainContract {
    interface Presenter extends BasePresenter{
        void displayLocalUser();
        void createOwnerQuiz(QuizModel quizModel);
        void getRoomQuizList(RoomModel roomModel);
        void expireParticipant(ParticipantModel participantModel, ResultModel resultModeld);
    }

    interface View extends BaseView<Presenter>{
        void onCurrentUserLoaded(UserModel userModel);
        void onCurrentUserEmpty();
        void onRoomQuizListLoaded(List<QuizModel> quizModelList);
        void onQuizDisplay();
        void onQuizEmpty();
        void onOwnerQuizRemoteSaved(QuizModel quizModel);
        void onExpiredParticipantTakenQuiz(ParticipantModel participantModel, ResultModel resultModel);
        void onError(Throwable t);
        void onProgressShow();
        void onProgressHide();
    }
}
