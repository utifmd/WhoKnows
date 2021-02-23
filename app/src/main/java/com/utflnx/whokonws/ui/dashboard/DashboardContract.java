package com.utflnx.whokonws.ui.dashboard;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.UserModel;

public interface DashboardContract {
    interface Presenter extends BasePresenter {
        void saveCurrentUser(UserModel currentUserModel);
        void signIn(UserModel userModel);
        void signUp(UserModel userModel);
    }

    interface View extends BaseView<Presenter> {
        void onSignedIn(UserModel currentUserModel);
        void onProfileEmpty();
        void onProfileSaved(UserModel userModel);
        void onProgressShow();
        void onProgressHide();
        void onError(Throwable t);
    }
}
