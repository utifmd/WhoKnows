package com.utflnx.whokonws.ui.dashboard;

import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;

public class DashboardPresenter implements DashboardContract.Presenter {
    DashboardContract.View mView;
    ProfileRepository profileRepository;

    public DashboardPresenter(DashboardContract.View mView, ProfileRepository profileRepository) {
        this.mView = mView;
        this.profileRepository = profileRepository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCurrentUser();
    }

    @Override
    public void loadCurrentUser() {
        mView.onProgressShow();
        profileRepository.getLocalProfile(new ProfileDataContract.LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                mView.onProfileLoaded(currentUserModel);
                mView.onProgressHide();
            }

            @Override
            public void onProfileEmpty() {
                mView.onProfileEmpty();
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable t) {
                mView.onError(t);
                mView.onProgressHide();
            }
        });
    }

    @Override
    public void saveCurrentUser(UserModel currentUserModel) {
        profileRepository.postLocalProfile(currentUserModel);
    }

    @Override
    public void signIn(UserModel userModel) {
        mView.onProgressShow();
        profileRepository.getRemoteProfile(userModel, new ProfileDataContract.LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                mView.onSignedIn(currentUserModel);
                mView.onProgressHide();
            }

            @Override
            public void onProfileEmpty() {
                mView.onProfileEmpty();
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable t) { // mView.onSignedOut();
                mView.onError(t);
                mView.onProgressHide();
            }
        });
    }

    @Override
    public void signUp(UserModel userModel) {
        mView.onProgressShow();
        profileRepository.postRemoteProfile(userModel, new ProfileDataContract.ActionProfileCallBack() {
            @Override
            public void onProfileResponse(UserModel currentUserModel) {
                mView.onProfileSaved(currentUserModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable t) {
                mView.onError(t);
                mView.onProgressHide();
            }
        });
    }

    @Override
    public void signOut() {

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
