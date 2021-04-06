package com.utflnx.whokonws.repo.result;

import android.content.Context;

import com.utflnx.whokonws.model.RoomModel;

public class ResultRepository implements ResultDataContract {
    ResultDataContract resultRemote;

    public ResultRepository(Context context) {
        resultRemote = new ResultRemote(context);
    }

    @Override
    public void getResultBy(RoomModel roomModel, LoadedResultCallback resultCallback) {
        resultRemote.getResultBy(roomModel, resultCallback);
    }
}
