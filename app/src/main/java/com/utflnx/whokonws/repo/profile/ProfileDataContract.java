package com.utflnx.whokonws.repo.profile;

import com.utflnx.whokonws.model.UserModel;

public interface ProfileDataContract {
    void getLocalProfile(LoadedProfileCallBack callBack);
    void postLocalProfile(UserModel userModel);
    void getRemoteProfile(UserModel userModel, LoadedProfileCallBack callBack);
    void postRemoteProfile(UserModel userModel, ActionProfileCallBack callBack);
    void updateUser(UserModel currentUserModel, ActionProfileCallBack callBack);
    void deleteLocalUser();
    void deleteRemoteUser(UserModel currentUserModel, ActionProfileCallBack callBack);

    interface LoadedProfileCallBack {
        void onProfileLoaded(UserModel currentUserModel);
        void onProfileEmpty();
        void onError(Throwable t);
    }

    interface ActionProfileCallBack {
        void onProfileResponse(UserModel currentUserModel);
        void onError(Throwable t);
    }
}
