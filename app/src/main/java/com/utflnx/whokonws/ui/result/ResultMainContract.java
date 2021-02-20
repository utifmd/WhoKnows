package com.utflnx.whokonws.ui.result;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.result.ResultDataContract;

import java.util.List;

public interface ResultMainContract {
    interface Presenter extends BasePresenter{
        void displayResult(RoomModel roomModel);
    }

    interface View extends BaseView<Presenter>{
        void onResultDisplay(List<ResultModel> resultModels);
        void onProgressShow();
        void onProgressHide();
        void onResultEmpty();
        void onError(Throwable t);
    }
}
