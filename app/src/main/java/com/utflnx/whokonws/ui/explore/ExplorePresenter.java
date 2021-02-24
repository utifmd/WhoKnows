package com.utflnx.whokonws.ui.explore;

import com.utflnx.whokonws.model.ExploreModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.explore.ExploreDataContract;
import com.utflnx.whokonws.repo.explore.ExploreRepository;

import java.util.List;

public class ExplorePresenter implements ExploreMainContract.Presenter {
    private  ExploreMainContract.View mView;
    private final ExploreRepository exploreRepository;

    public ExplorePresenter(ExploreMainContract.View view, ExploreRepository exploreRepository) {
        this.mView = view;
        this.exploreRepository = exploreRepository;

        mView.setPresenter(this);
    }

    @Override
    public void displayExplore() {
        mView.onProgressShow();
        exploreRepository.getExplores(new ExploreDataContract.LoadedExploreCallback() {
            @Override
            public void onExploreLoaded(List<ExploreModel> exploreModels) {
                mView.onExploreLoaded(exploreModels);
                mView.onProgressHide();
            }

            @Override
            public void onExploreEmpty() {
                mView.onExploreEmpty();
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
    public void start() {
        displayExplore();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
