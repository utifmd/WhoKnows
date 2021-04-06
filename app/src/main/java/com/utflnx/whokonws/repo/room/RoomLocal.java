package com.utflnx.whokonws.repo.room;

import android.content.Context;

import com.utflnx.whokonws.api.module.LocalModule;
import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomLocal implements RoomDataContract.LocalListener {
    private LocalService localService;
    private ExecutorService executorService;

    public RoomLocal(Context context) {
        LocalModule localModule = LocalModule.init(context);

        localService = localModule.provideRoomService();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getLocalRoom(RoomDataContract.LoadedRoomCallback callBack) {
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
}
