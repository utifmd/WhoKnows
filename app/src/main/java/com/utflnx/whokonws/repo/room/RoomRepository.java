package com.utflnx.whokonws.repo.room;

import android.content.Context;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

public class RoomRepository implements RoomDataContract {
    private RoomDataContract roomRemote, roomLocal;

    public RoomRepository(Context context) {
        roomRemote = new RoomRemote(context);
        roomLocal = new RoomLocal(context);
    }

    @Override
    public void getOwnerRoom(UserModel currentUserModel, LoadedRoomListCallback callback) {
        roomRemote.getOwnerRoom(currentUserModel, callback);
    }

    @Override
    public void postOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack) {
        roomRemote.postOwnerRoom(roomModel, callBack);
    }

    @Override
    public void deleteOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack) {
        roomRemote.deleteOwnerRoom(roomModel, callBack);
    }

    @Override
    public void getRemoteRoom(String roomId, LoadedRoomCallback callBack) {
        roomRemote.getRemoteRoom(roomId, callBack);
    }

    @Override
    public void getLocalRoom(LoadedRoomCallback callBack) {
        roomLocal.getLocalRoom(callBack);
    }

    @Override
    public void postLocalRoom(RoomModel roomModel) {
        roomLocal.postLocalRoom(roomModel);
    }

    @Override
    public void postPublicParticipant(ParticipantModel participantModel, ActionParticipantCallBack participantCallBack) {
        roomRemote.postPublicParticipant(participantModel, participantCallBack);
    }

    @Override
    public void updateCurrentRoom(RoomModel currentRoom, ActionRoomCallBack roomCallBack) {
        roomRemote.updateCurrentRoom(currentRoom, roomCallBack);
    }
}
