package com.utflnx.whokonws.repo.explore;

import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface ExploreDataContract {
    void getUsers(LoadedExploreCallback loadedExploreCallback);
    void postUser(UserModel userModel, ActionExploreCallback actionExploreCallback);
    void deleteUser(UserModel userModel, ActionExploreCallback actionExploreCallback);
    void updateUser(UserModel userModel, ActionExploreCallback actionExploreCallback);

    interface LoadedExploreCallback {
        void onUsersLoaded(List<UserModel> userModelList);
        void onUsersEmpty();
        void onError(Throwable e);
    }

    interface ActionExploreCallback {
        void onUserResponse(UserModel userModel);
        void onError(Throwable e);
    }
}
