package com.utflnx.whokonws.ui.profile;

import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;

public class ProfilePresenter implements ProfileMainContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private ProfileMainContract.View mView;
    private final ProfileRepository profileRepository;
    private final QuizRepository quizRepository;

    public ProfilePresenter(ProfileMainContract.View view, ProfileRepository repository, QuizRepository quizRepository) {
        this.mView = view;
        this.profileRepository = repository;
        this.quizRepository = quizRepository;

        view.setPresenter(this);
    }

    @Override
    public void displayLocalUser() {
        mView.onProgressShow();
        profileRepository.getLocalProfile(new ProfileDataContract.LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                mView.onProfileLocalLoaded(currentUserModel);
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
    public void displayRemoteUser(UserModel currentUserModel) {
        profileRepository.getRemoteProfile(currentUserModel, new ProfileDataContract.LoadedProfileCallBack() {
            @Override
            public void onProfileLoaded(UserModel currentUserModel) {
                mView.onProfileRemoteLoaded(currentUserModel);
            }

            @Override
            public void onProfileEmpty() {
                mView.onProfileEmpty();
            }

            @Override
            public void onError(Throwable t) {
                mView.onError(t);
            }

        });
    }

    @Override
    public void editUser(UserModel currentUserModel) {
        mView.onProgressShow();
        profileRepository.updateUser(currentUserModel, new ProfileDataContract.ActionProfileCallBack() {
            @Override
            public void onProfileResponse(UserModel currentUserModel) {
                mView.onProfileUpdated(currentUserModel);
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
    public void removeUser(UserModel currentUserModel) {
        mView.onProgressShow();
        profileRepository.deleteRemoteUser(currentUserModel, new ProfileDataContract.ActionProfileCallBack() {
            @Override
            public void onProfileResponse(UserModel currentUserModel) {
                mView.onProfileDeleted(currentUserModel);
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
        profileRepository.deleteLocalUser();
        quizRepository.deleteQuizLocal();
        mView.onProfileEmpty();
    }

    @Override
    public void start() {
        displayLocalUser();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
