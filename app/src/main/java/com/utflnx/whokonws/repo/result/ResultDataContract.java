package com.utflnx.whokonws.repo.result;

import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;

import java.util.List;

public interface ResultDataContract {
    void getResultBy(RoomModel roomModel, LoadedResultCallback resultCallback);

    interface LoadedResultCallback{
        void onResultLoaded(List<ResultModel> resultModels);
        void onResultEmpty();
        void onError(Throwable t);
    }

    interface ActionResultCallback{
        void onResultResponse(ResultModel resultModel);
        void onResultError(Throwable t);
    }
}
