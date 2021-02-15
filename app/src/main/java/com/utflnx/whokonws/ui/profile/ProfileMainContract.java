package com.utflnx.whokonws.ui.profile;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.UserModel;

public interface ProfileMainContract {
    //// input, process
    interface Presenter extends BasePresenter {
        void displayLocalUser();
        void displayRemoteUser(UserModel currentUserModel);
        void editUser(UserModel currentUserModel);
        void removeUser(UserModel currentUserModel);
        void signOut();
    }

    //// output
    interface View extends BaseView<Presenter> {
        void onProfileLocalLoaded(UserModel currentUserModel);
        void onProfileRemoteLoaded(UserModel currentUserModel);
        void onProfileUpdated(UserModel currentUserModel);
        void onProfileDeleted(UserModel currentUserModel);
        void onProfileEmpty();
        void onError(Throwable t);
        void onProgressShow();
        void onProgressHide();
    }
}
