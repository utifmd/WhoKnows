package com.utflnx.whokonws.repo.participant;

import android.content.Context;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

public class ParticipateRepository implements ParticipantDataContract {
    ParticipantDataContract remoteParticipate;

    public ParticipateRepository(Context context) {
        this.remoteParticipate = new ParticipateRemote(context);
    }

    @Override
    public void getCurrentParticipate(UserModel currentUser, RoomModel roomModel, LoadedParticipantCallback participantCallback) {
        remoteParticipate.getCurrentParticipate(currentUser, roomModel, participantCallback);
    }

    @Override
    public void updateCurrentParticipant(ParticipantModel participantModel, ResultModel resultModel, ActionParticipantCallback actionParticipantCallback) {
        remoteParticipate.updateCurrentParticipant(participantModel, resultModel, actionParticipantCallback);
    }
}
