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
    public void getExplores(LoadedExploreCallback loadedExploreCallback) {
        mExploreRemoteSource.getExplores(loadedExploreCallback);
    }
}
