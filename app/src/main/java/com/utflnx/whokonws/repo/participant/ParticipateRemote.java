package com.utflnx.whokonws.repo.participant;

import android.content.Context;
import android.util.Log;

import com.utflnx.whokonws.api.module.RemoteModule;
import com.utflnx.whokonws.api.service.RemoteService;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.APIRequestModel;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public void getCurrentParticipate(UserModel currentUser, RoomModel roomModel, LoadedParticipantCallback participantCallback) {
        LinkedHashMap<String, String> container = new LinkedHashMap<>();
        container.put("userId", currentUser.getUserId());
        container.put("roomId", roomModel.getRoomId());
/*
* $fetchCoupleBy = '{
    "about": "fetch_couple_by",
    "database": "_who_knows",
    "table": {
        "name": ["_table_room"],
        "container": {
            "roomId": "83c8c2c9-204c-45e6-bb16-e6221127c7ff",
            "userId": "58eb70f4-0e83-4bf0-aac5-c40f9bad3565"
        }
    }
}';
* */
        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_FETCH_COUPLE_BY, new String[]{ ListObjects.TABLE_PARTICIPANT }, container, null, null);

        remoteService.getParticipantBy(requestModel).enqueue(new Callback<List<ParticipantModel>>() {
            @Override
            public void onResponse(Call<List<ParticipantModel>> call, Response<List<ParticipantModel>> response) {
                if (response.body() != null){
                    if (response.body().size() == 0){
                        participantCallback.onEmptyParticipant();
                    }else {
                        for (ParticipantModel model: response.body()){
                            if (!model.isExpired())
                                participantCallback.onParticipateExist(model);
                            else if (model.isExpired())
                                participantCallback.onParticipateExpired();
                            else
                                participantCallback.onError(new Throwable("Invalid server response."));

                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ParticipantModel>> call, Throwable t) {
                participantCallback.onError(new Throwable());
            }
        });
    }

    @Override
    public void updateCurrentParticipant(ParticipantModel participantModel, ResultModel resultModel, ActionParticipantCallback actionParticipantCallback) {
        participantModel.setExpire(true);
        APIRequestModel requestModel = RemoteModule.passingRequestModel(
                ListObjects.ABOUT_UPDATE_POST_COUPLE,
                new String[]{ ListObjects.TABLE_PARTICIPANT, ListObjects.TABLE_RESULT },
                participantModel, resultModel,
                "participantId = '"+participantModel.getParticipantId()+"'"
        );

        remoteService.updateParticipant(requestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null)
                    actionParticipantCallback.onParticipantResponse(participantModel, resultModel);
                else
                    actionParticipantCallback.onError(new Throwable("Invalid server request."));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                actionParticipantCallback.onError(t);
            }
        });
    }
}
