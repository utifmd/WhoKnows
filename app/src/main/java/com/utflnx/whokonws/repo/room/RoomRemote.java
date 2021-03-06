package com.utflnx.whokonws.repo.room;

import android.content.Context;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomRemote implements RoomDataContract.RemoteListener{
    private final RemoteService remoteService;

    public RoomRemote(Context context) {
        this.remoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void getOwnerRoom(UserModel currentUserModel, RoomDataContract.LoadedRoomListCallback callback) {
        HashMap<String, String> container = new HashMap<>();
        container.put("userId", currentUserModel.getUserId());

        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(ListObjects.ABOUT_FETCH_SINGLE, new String[]{ ListObjects.TABLE_ROOM }, container, null, null);

        remoteService.getOwnerRoom(APIRequestModel).enqueue(new Callback<List<RoomModel>>() {
            @Override
            public void onResponse(Call<List<RoomModel>> call, Response<List<RoomModel>> response) {
                if(response.body() != null) {
                    if(response.body().size() != 0) callback.onOwnerRoomLoaded(response.body());
                    else callback.onOwnerRoomEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<RoomModel>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void postOwnerRoom(RoomModel roomModel, RoomDataContract.ActionRoomCallBack callBack) {
        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(ListObjects.ABOUT_POST_ONLY, new String[]{ListObjects.TABLE_ROOM}, roomModel, null, null);

        remoteService.postOwnerRoom(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    callBack.onRoomResponse(roomModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void deleteOwnerRoom(RoomModel roomModel, RoomDataContract.ActionRoomCallBack callBack) {
        HashMap<String, String> container = new HashMap<>();
        container.put("roomId", roomModel.getRoomId());

        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(ListObjects.ABOUT_DELETE_QUAD, new String[]{
                ListObjects.TABLE_ROOM,
                ListObjects.TABLE_QUIZ,
                ListObjects.TABLE_RESULT,
                ListObjects.TABLE_PARTICIPANT
        }, container, null, null);

        remoteService.deleteOwnerRoom(APIRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null)
                    callBack.onRoomResponse(roomModel);
                else
                    callBack.onError(new Throwable("invalid response body."));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void getRemoteRoom(String roomId, RoomDataContract.LoadedRoomCallback callBack) {
        HashMap<String, String> container = new HashMap<>();
        container.put("roomId", roomId);

        APIRequestModel APIRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_SINGLE, new String[]{ListObjects.TABLE_ROOM}, container, null, null);

        remoteService.getRoomById(APIRequestModel).enqueue(new Callback<List<RoomModel>>() {
            @Override
            public void onResponse(Call<List<RoomModel>> call, Response<List<RoomModel>> response) {
                if (response.body() != null) {
                    if (response.body().size() != 0) callBack.onRoomLoaded(response.body().get(0));
                    else callBack.onError(new Throwable("room not found, please check the id."));
                }
            }

            @Override
            public void onFailure(Call<List<RoomModel>> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void postPublicParticipant(ParticipantModel participantModel, RoomDataContract.ActionParticipantCallBack participantCallBack) {
        APIRequestModel apiRequestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_POST_ONLY,
                new String[]{ListObjects.TABLE_PARTICIPANT},
                participantModel, null, null
        );

        remoteService.postParticipant(apiRequestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null)
                    participantCallBack.onRoomResponse(participantModel);
                else
                    participantCallBack.onError(new Throwable("Invalid request."));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                participantCallBack.onError(t);
            }
        });
    }

    @Override
    public void updateCurrentRoom(RoomModel currentRoom, RoomDataContract.ActionRoomCallBack roomCallBack) {
        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_UPDATE_ONLY, new String[]{ListObjects.TABLE_ROOM}, currentRoom, null, "roomId = '"+currentRoom.getRoomId()+"'");

        remoteService.updateOwnerRoom(requestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null){
                    roomCallBack.onRoomResponse(currentRoom);
                } else roomCallBack.onError(new Throwable("Invalid server response."));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                roomCallBack.onError(t);
            }
        });
    }
}
