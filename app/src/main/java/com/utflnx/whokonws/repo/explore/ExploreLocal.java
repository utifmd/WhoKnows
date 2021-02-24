package com.utflnx.whokonws.repo.explore;

import android.content.Context;

import com.utflnx.whokonws.api.module.LocalModule;
import com.utflnx.whokonws.api.service.LocalService;
import com.utflnx.whokonws.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExploreLocal implements ExploreDataContract {
    private final LocalService mLocalService;
    private final ExecutorService mExecutorService;

    public ExploreLocal(Context context) {
        LocalModule localModule = LocalModule.init(context);

        mExecutorService = Executors.newSingleThreadExecutor();
        mLocalService = localModule.provideRoomService();
    }

    @Override
    public void getExplores(LoadedExploreCallback loadedExploreCallback) { }
}
