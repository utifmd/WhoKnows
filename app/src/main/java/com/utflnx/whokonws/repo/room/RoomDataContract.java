package com.utflnx.whokonws.repo.room;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface RoomDataContract {
    void getOwnerRoom(UserModel currentUserModel, LoadedRoomListCallback callback);
    void postOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack);
    void deleteOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack);
    void getRemoteRoom(String roomId, LoadedRoomCallback callBack);
    void getLocalRoom(LoadedRoomCallback callBack);
    void postLocalRoom(RoomModel roomModel);
    void postPublicParticipant(ParticipantModel participantModel, ActionParticipantCallBack participantCallBack);

//    void postQuestion(Quiz quiz);
//    void deleteQuestion(Quiz quiz);
//
//    void getParticipants(LoadedRoomCallback callback);
//    void getResults(List<Result> results);

    interface LoadedRoomListCallback {
        void onOwnerRoomLoaded(List<RoomModel> roomModels);
        void onOwnerRoomEmpty();
        void onError(Throwable t);
    }

    interface LoadedRoomCallback {
        void onRoomLoaded(RoomModel rooms);
        void onRoomEmpty();
        void onError(Throwable t);
    }

    interface ActionRoomCallBack{
        void onRoomResponse(RoomModel roomModel);
        void onError(Throwable t);
    }

    interface ActionParticipantCallBack{
        void onRoomResponse(ParticipantModel participantModel);
        void onError(Throwable t);
    }
}
