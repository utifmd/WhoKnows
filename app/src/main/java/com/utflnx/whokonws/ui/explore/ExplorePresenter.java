package com.utflnx.whokonws.ui.explore;

import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.explore.ExploreDataContract;
import com.utflnx.whokonws.repo.explore.ExploreRepository;

import java.util.List;

public class ExplorePresenter implements ExploreMainContract.Presenter {
    private  ExploreMainContract.View mView;
    private final ExploreRepository exploreRepository;

    public ExplorePresenter(ExploreMainContract.View mView, ExploreRepository exploreRepository) {
        this.mView = mView;
        this.exploreRepository = exploreRepository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        displayUsers();
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void displayUsers() {
        mView.onProgressShow();
        exploreRepository.getUsers(new ExploreDataContract.LoadedExploreCallback() {
            @Override
            public void onUsersLoaded(List<UserModel> userModelList) {
                mView.onUsersLoaded(userModelList);
                mView.onProgressHide();
            }

            @Override
            public void onUsersEmpty() {
                mView.onUsersEmpty();
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
                mView.onProgressHide();
            }
        });
    }

    @Override
    public void createUser(UserModel userModel) {
        mView.onProgressShow();
        exploreRepository.postUser(userModel, new ExploreDataContract.ActionExploreCallback() {
            @Override
            public void onUserResponse(UserModel userModel) {
                mView.onUserSaved(userModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
                mView.onProgressHide();
            }
        }); // exploreRepository.postUser(user, mView::onUserSaved);
    }

    @Override
    public void editUser(UserModel userModel) {
        mView.onProgressShow();
        exploreRepository.updateUser(userModel, new ExploreDataContract.ActionExploreCallback() {
            @Override
            public void onUserResponse(UserModel userModel) {
                mView.onUserUpdated(userModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
                mView.onProgressHide();
            }
        });
    }

    @Override
    public void removeUser(UserModel userModel) {
        mView.onProgressShow();
        exploreRepository.deleteUser(userModel, new ExploreDataContract.ActionExploreCallback() {
            @Override
            public void onUserResponse(UserModel userModel) {
                mView.onUserDeleted(userModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
                mView.onProgressHide();
            }
        });
    }
}
