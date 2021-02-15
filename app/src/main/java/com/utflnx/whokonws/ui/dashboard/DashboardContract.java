package com.utflnx.whokonws.ui.dashboard;

import com.utflnx.whokonws.BasePresenter;
import com.utflnx.whokonws.BaseView;
import com.utflnx.whokonws.model.UserModel;

public interface DashboardContract {
    interface Presenter extends BasePresenter {
        void loadCurrentUser();
        void saveCurrentUser(UserModel currentUserModel);
        void signIn(UserModel userModel);
        void signOut();
        void signUp(UserModel userModel);
    }

    interface View extends BaseView<Presenter> {
        void onProfileLoaded(UserModel currentUserModel);
        void onSignedIn(UserModel currentUserModel);
        void onProfileEmpty();
        void onProfileSaved(UserModel userModel);
        void onProfileDeleted(UserModel userModel);
        void onSignedOut();
        void onProgressShow();
        void onProgressHide();
        void onError(Throwable t);
    }
}
