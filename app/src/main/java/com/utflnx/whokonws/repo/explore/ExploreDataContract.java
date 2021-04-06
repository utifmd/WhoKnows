package com.utflnx.whokonws.repo.explore;

import com.utflnx.whokonws.model.ExploreModel;
import com.utflnx.whokonws.model.UserModel;

import java.util.List;

public interface ExploreDataContract {
    void getExplores(LoadedExploreCallback loadedExploreCallback);

    interface LoadedExploreCallback {
        void onExploreLoaded(List<ExploreModel> exploreModels);
        void onExploreEmpty();
        void onError(Throwable e);
    }

    interface ActionExploreCallback {
        void onExploreResponse(ExploreModel exploreModel);
        void onError(Throwable e);
    }
}
