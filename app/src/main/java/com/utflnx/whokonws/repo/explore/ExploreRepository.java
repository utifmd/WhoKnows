package com.utflnx.whokonws.repo.explore;

import android.content.Context;

import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public class ExploreRepository implements ExploreDataContract {
    ExploreDataContract mExploreRemoteSource, mExploreLocalSource;

    public ExploreRepository(Context context) {
        mExploreRemoteSource = new ExploreRemote(context);
        mExploreLocalSource = new ExploreLocal(context);
    }

    @Override
    public void getUsers(LoadedExploreCallback mLoadedExploreCallback) {
        mExploreRemoteSource.getUsers(new LoadedExploreCallback() {
            @Override
            public void onUsersLoaded(List<UserModel> userModelList) {
                mLoadedExploreCallback.onUsersLoaded(userModelList);
            }

            @Override
            public void onUsersEmpty() {
                mLoadedExploreCallback.onUsersEmpty();
            }

            @Override
            public void onError(Throwable e) {
                mLoadedExploreCallback.onError(e);
            }
        });
    }

    @Override
    public void postUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        mExploreRemoteSource.postUser(userModel, actionExploreCallback);
    }

    @Override
    public void deleteUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        mExploreRemoteSource.deleteUser(userModel, actionExploreCallback);

    }

    @Override
    public void updateUser(UserModel userModel, ActionExploreCallback actionExploreCallback) {
        mExploreRemoteSource.updateUser(userModel, actionExploreCallback);

    }
}
