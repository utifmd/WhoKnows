package com.utflnx.whokonws.ui.room.publicity;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.List;

public interface RoomMainContract {
    interface Presenter extends BasePresenter {
        void joinRoom(String roomId);
        void takingRoom(ParticipantModel participantModel);
        // void selectRoomItem(Room room); // void displayRoom(String roomId);
        void saveCurrentRoom(RoomModel roomModel);
        void removeCurrentRoom(RoomModel roomModel);
        void displayCurrentRoom();
        void displayCurrentParticipate(RoomModel roomModel);
        void displayRoomQuizList(RoomModel roomModel);
        void selectItemQuiz(QuizModel quizModel);
    }

    interface View extends BaseView<RoomMainContract.Presenter> {
        void onRoomLocalLoaded(RoomModel roomModel);
        void onRoomRemoteLoaded(RoomModel roomModel);
        void onRoomEmpty();
        void onExistParticipation(ParticipantModel participantModels);
        void onExpiredParticipate();
        void onParticipantEmpty();
        void onQuestionsEmpty();
        void onRoomJoinSaved(RoomModel roomModel); // void onRoomItemSelected(Room room);
        void onRoomTakenSaved(ParticipantModel participantModel);
        void onQuestionsLoaded(List<QuizModel> quizModelList);
        void onItemQuizSelected(QuizModel quizModel);
        void onError(Throwable t);
        void onProgressShow();
        void onProgressHide();
    }
}
