package com.utflnx.whokonws.repo.room;

import android.content.Context;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

public class RoomRepository implements RoomDataContract.LocalListener, RoomDataContract.RemoteListener {
    private final RoomDataContract.RemoteListener roomRemote;
    private final RoomDataContract.LocalListener roomLocal;

    public RoomRepository(Context context) {
        roomRemote = new RoomRemote(context);
        roomLocal = new RoomLocal(context);
    }

    @Override
    public void postPublicParticipant(ParticipantModel participantModel, RoomDataContract.ActionParticipantCallBack participantCallBack) {
        roomRemote.postPublicParticipant(participantModel, participantCallBack);
    }

    @Override
    public void updateCurrentRoom(RoomModel currentRoom, RoomDataContract.ActionRoomCallBack roomCallBack) {
        roomRemote.updateCurrentRoom(currentRoom, roomCallBack);
    }

    @Override
    public void getOwnerRoom(UserModel currentUserModel, RoomDataContract.LoadedRoomListCallback callback) {
        roomRemote.getOwnerRoom(currentUserModel, callback);
    }

    @Override
    public void postOwnerRoom(RoomModel roomModel, RoomDataContract.ActionRoomCallBack callBack) {
        roomRemote.postOwnerRoom(roomModel, callBack);
    }

    @Override
    public void deleteOwnerRoom(RoomModel roomModel, RoomDataContract.ActionRoomCallBack callBack) {
        roomRemote.deleteOwnerRoom(roomModel, callBack);
    }

    @Override
    public void getRemoteRoom(String roomId, RoomDataContract.LoadedRoomCallback callBack) {
        roomRemote.getRemoteRoom(roomId, callBack);
    }

    @Override
    public void getLocalRoom(RoomDataContract.LoadedRoomCallback callBack) {
        roomLocal.getLocalRoom(callBack);
    }

    @Override
    public void postLocalRoom(RoomModel roomModel) {
        roomLocal.postLocalRoom(roomModel);
    }
}
