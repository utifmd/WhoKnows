package com.utflnx.whokonws.repo.participant;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipateRemote implements ParticipantDataContract {
    private final String TAG = getClass().getSimpleName();
    private final RemoteService remoteService;

    public ParticipateRemote(Context context) {
        remoteService = RemoteModule.provideMySqlService();
    }

    @Override
    public void getCurrentParticipate(RoomModel roomModel, LoadedParticipantCallback participantCallback) {
        HashMap<String, String> container = new HashMap<>();
        container.put("userId", roomModel.getUserId());

        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_SINGLE, new String[]{ ListObjects.TABLE_PARTICIPANT }, container, null);

        remoteService.getParticipantBy(requestModel).enqueue(new Callback<List<ParticipantModel>>() {
            @Override
            public void onResponse(Call<List<ParticipantModel>> call, Response<List<ParticipantModel>> response) {
                if (response.body() != null){
                    if (response.body().size() == 0){
                        participantCallback.onEmptyParticipant();
                    }else {
                        for (ParticipantModel model: response.body()){
                            if (model.getRoomId().equals(roomModel.getRoomId()) && !model.isExpired())
                                participantCallback.onParticipateExist(model);

                            else if (model.getRoomId().equals(roomModel.getRoomId()) && model.isExpired())
                                participantCallback.onParticipateExpired();

                            break;
                        }
                    }
//                    if (response.body().size() != 0) {
//                        for (ParticipantModel model: response.body()){ //fix logic when
//                            if (model.getRoomId().equals(roomModel.getRoomId()) && !model.isExpired())
//                                participantCallback.onCurrentParticipate(model);
//
//                            else if (model.getRoomId().equals(roomModel.getRoomId()) && model.isExpired())
//                                participantCallback.onParticipateExpired();
//
//                            else if (model.getRoomId().equals(roomModel.getRoomId()))
//                                participantCallback.onParticipateExist(model);
//
//                            break;
//                        }
//                    } else
//                        participantCallback.onEmptyParticipant();
                }
            }

            @Override
            public void onFailure(Call<List<ParticipantModel>> call, Throwable t) {
                participantCallback.onError(new Throwable());
            }
        });
    }

    @Override
    public void updateCurrentParticipant(ParticipantModel participantModel, ActionParticipantCallback actionParticipantCallback) {
        participantModel.setExpire(true);
        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_UPDATE_ONLY, new String[]{ ListObjects.TABLE_PARTICIPANT }, participantModel,
                "participantId = '"+participantModel.getParticipantId()+"'"
        );

        Log.d(TAG, participantModel.toString());

        remoteService.updateParticipant(requestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null)
                    actionParticipantCallback.onParticipantResponse(participantModel);
                else
                    actionParticipantCallback.onError(new Throwable("Invalid server request."));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
