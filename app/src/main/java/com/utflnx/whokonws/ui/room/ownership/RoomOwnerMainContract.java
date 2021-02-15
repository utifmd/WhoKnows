package com.utflnx.whokonws.ui.room.ownership;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface RoomOwnerMainContract {
    //// input, process
    interface Presenter extends BasePresenter{
//        void displayRoom();
        void displayOwnerRoom(UserModel currentUserModel);
        void createRoom(RoomModel roomModel);
        void removeRoom(RoomModel roomModel);
        void selectRoomItem(RoomModel roomModel);
        boolean selectLongRoomItem(RoomModel roomModel, int position);
//
//        void displayQuestions();
//        void createQuestion(Quiz quiz);
//        void removeQuestion(Quiz quiz);
//
//        void displayParticipants();
//        void displayResults(List<Result> results);
    }

    //// output
    interface View extends BaseView<Presenter> {
        void onRoomOwnerLoaded(List<RoomModel> roomModels);
        void onRoomOwnerEmpty();
        void onRoomOwnerSaved(RoomModel roomModel);
        void onRoomItemSelected(RoomModel roomModel);
        boolean onRoomItemLongSelected(RoomModel roomModel, int pos);
        void onRoomDeleted(RoomModel roomModel);
        void onRoomLoaded(RoomModel roomModel);
//        void onQuestionsLoaded(List<Quiz> questions);
//        void onQuestionSaved(Quiz quiz);
//        void onQuestionDeleted(Quiz quiz);
//        void onParticipantsLoaded(List<User> users);
//        void onResultsLoaded(List<Result> results);
        void onError(Throwable t);
        void onProgressShow();
        void onProgressHide();
    }
}
