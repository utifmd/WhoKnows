package com.utflnx.whokonws.ui.explore;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface ExploreMainContract {
    //// input, process
    interface Presenter extends BasePresenter {
        void displayUsers();
        void createUser(UserModel userModel);
        void editUser(UserModel userModel);
        void removeUser(UserModel userModel);
    }

    //// output
    interface View extends BaseView<Presenter> {
        void onUsersLoaded(List<UserModel> userModelList);
        void onUserSaved(UserModel userModel);
        void onUserUpdated(UserModel userModel);
        void onUserDeleted(UserModel userModel);
        void onProgressShow();
        void onProgressHide();
        void onUsersEmpty();
        void onError(Throwable e);
    }
}
