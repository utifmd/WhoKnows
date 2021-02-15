package com.utflnx.whokonws.repo.profile;

import android.content.Context;

import com.utflnx.whokonws.model.UserModel;

public class ProfileRepository implements ProfileDataContract {
    ProfileDataContract profileRemote, profileLocal;

    public ProfileRepository(Context context) {
        this.profileRemote = new ProfileRemote(context);
        this.profileLocal = new ProfileLocal(context); // this.preferences = ListObjects.provideSharedPrefs(context);
    }

    @Override
    public void getLocalProfile(LoadedProfileCallBack callBack) {
        profileLocal.getLocalProfile(new LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                callBack.onProfileLoaded(currentUserModel);
            }

            @Override
            public void onProfileEmpty() {
                callBack.onProfileEmpty();
            }

            @Override
            public void onError(Throwable t) {
                callBack.onError(t);
            }
        });
    }

    @Override
    public void getRemoteProfile(UserModel userModel, LoadedProfileCallBack callBack) {
        profileRemote.getRemoteProfile(userModel, new LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                callBack.onProfileLoaded(currentUserModel);
            }

            @Override
            public void onProfileEmpty() {
                callBack.onProfileEmpty();
            }

            @Override
            public void onError(Throwable t) {
                callBack.onError(t);
            }

        });
    }

    @Override
    public void postRemoteProfile(UserModel userModel, ActionProfileCallBack callBack) {
        profileRemote.postRemoteProfile(userModel, callBack); //.postUser(user, actionExploreCallback);
    }

    @Override
    public void postLocalProfile(UserModel userModel) {
        profileLocal.postLocalProfile(userModel);
    }


    @Override
    public void updateUser(UserModel currentUserModel, ActionProfileCallBack callBack) {
        profileRemote.updateUser(currentUserModel, callBack);
    }

    @Override
    public void deleteLocalUser() {
        profileLocal.deleteLocalUser();
    }

    @Override
    public void deleteRemoteUser(UserModel currentUserModel, ActionProfileCallBack callBack) {
        profileRemote.deleteRemoteUser(currentUserModel, callBack);
    }
}
