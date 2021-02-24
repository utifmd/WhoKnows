package com.utflnx.whokonws.ui.explore;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.ExploreModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface ExploreMainContract {
    interface Presenter extends BasePresenter {
        void displayExplore();
    }

    interface View extends BaseView<Presenter> {
        void onExploreLoaded(List<ExploreModel> exploreModels);
        void onExploreEmpty();
        void onProgressShow();
        void onProgressHide();
        void onError(Throwable e);
    }
}
