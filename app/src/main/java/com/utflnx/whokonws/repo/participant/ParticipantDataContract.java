package com.utflnx.whokonws.repo.participant;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface ParticipantDataContract {
    void getCurrentParticipate(UserModel currentUser, RoomModel roomModel, LoadedParticipantCallback participantCallback);
    void updateCurrentParticipant(ParticipantModel participantModel, ResultModel resultModel, ActionParticipantCallback actionParticipantCallback);

    interface LoadedParticipantCallback{
        void onParticipateExist(ParticipantModel participantModels);
        void onParticipateExpired();
        void onEmptyParticipant();
        void onError(Throwable t);
    }

    interface ActionParticipantCallback{
        void onParticipantResponse(ParticipantModel participant, ResultModel result);
        void onError(Throwable t);
    }
}
