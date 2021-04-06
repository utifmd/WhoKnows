package com.utflnx.whokonws.repo.profile;

import android.content.Context;

import com.utflnx.whokonws.api.module.LocalModule;
import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileLocal implements ProfileDataContract {
    private final LocalService mLocalService;
    private final ExecutorService mExecutorService;

    public ProfileLocal(Context context) {
        LocalModule localModule = LocalModule.init(context);

        mExecutorService = Executors.newSingleThreadExecutor();
        mLocalService = localModule.provideRoomService();
    }

    @Override
    public void getLocalProfile(LoadedProfileCallBack callBack) {
        mExecutorService.execute(() -> {
            if(mLocalService.getCurrentUser() != null){
                callBack.onProfileLoaded(mLocalService.getCurrentUser());
            }else {
                callBack.onProfileEmpty();
            }
        });
    }

    @Override
    public void postLocalProfile(UserModel userModel) {
        mExecutorService.execute(() ->
                mLocalService.addUser(userModel)
        );
    }

    @Override
    public void deleteLocalUser() {
        mExecutorService.execute(mLocalService::deleteUser);
    }

    @Override
    public void updateUser(UserModel currentUserModel, ActionProfileCallBack callBack) {}

    @Override
    public void deleteRemoteUser(UserModel currentUserModel, ActionProfileCallBack callBack) {}

    @Override
    public void getRemoteProfile(UserModel userModel, LoadedProfileCallBack callBack) {}

    @Override
    public void postRemoteProfile(UserModel userModel, ActionProfileCallBack callBack) {}
}