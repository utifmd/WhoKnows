package com.utflnx.whokonws.repo.participant;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.List;

public interface ParticipantDataContract {
    void getCurrentParticipate(RoomModel roomModel, LoadedParticipantCallback participantCallback);
    void updateCurrentParticipant(ParticipantModel participantModel, ActionParticipantCallback actionParticipantCallback);

    interface LoadedParticipantCallback{
        void onParticipateExist(ParticipantModel participantModels);
        void onParticipateExpired();
        void onEmptyParticipant();
        void onError(Throwable t);
    }

    interface ActionParticipantCallback{
        void onParticipantResponse(ParticipantModel participantModel);
        void onError(Throwable t);
    }
}
