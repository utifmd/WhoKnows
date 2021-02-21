package com.utflnx.whokonws.repo.room;

import android.content.Context;

import com.utflnx.whokonws.api.module.LocalModule;
import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomLocal implements RoomDataContract {
    private LocalService localService;
    private ExecutorService executorService;

    public RoomLocal(Context context) {
        LocalModule localModule = LocalModule.init(context);

        localService = localModule.provideRoomService();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getLocalRoom(LoadedRoomCallback callBack) {
        executorService.execute(() -> {
            if (localService.getCurrentRoom() != null){
                callBack.onRoomLoaded(localService.getCurrentRoom());
            } else {
                callBack.onRoomEmpty();
            }
        });
    }

    @Override
    public void postLocalRoom(RoomModel roomModel) {
        executorService.execute(() -> localService.addCurrentRoom(roomModel));
    }

    @Override
    public void postPublicParticipant(ParticipantModel participantModel, ActionParticipantCallBack participantCallBack) { }

    @Override
    public void updateCurrentRoom(RoomModel currentRoom, ActionRoomCallBack roomCallBack) { }

    @Override
    public void getOwnerRoom(UserModel currentUserModel, LoadedRoomListCallback callback) { }

    @Override
    public void postOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack) { }

    @Override
    public void deleteOwnerRoom(RoomModel roomModel, ActionRoomCallBack callBack) { }

    @Override
    public void getRemoteRoom(String roomId, LoadedRoomCallback callBack) { }
}
