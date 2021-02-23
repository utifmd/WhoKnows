package com.utflnx.whokonws.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.explore.ExploreRepository;

import java.util.List;

public class ExploreFragment extends Fragment implements ExploreMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private ExploreMainContract.Presenter mPresenter;
    private ExploreRepository mRepository;

    private UserModel mCurrentUser = null;

    @Override
    public void setPresenter(ExploreMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mRepository = new ExploreRepository(context);
    }

    public static ExploreFragment createInstance(UserModel currentUser){
        ExploreFragment explore = new ExploreFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        explore.setArguments(bundle);

        return explore;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        if (getArguments() != null)
            mCurrentUser = (UserModel) getArguments().getSerializable(ListObjects.KEY_CURRENT_USER);

        new ExplorePresenter(this, mRepository);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        rootView.findViewById(R.id.singleExploreText).setOnClickListener(view -> {
            Log.d(TAG, "singleTextExplore clicked!");

        });

        return rootView;
    }


    @Override
    public void onExploreLoaded(RoomModel roomModel) {
        Log.d(TAG, "onExploreLoaded");
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "progress...");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "progress done.");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "Explore error "+ e.getLocalizedMessage());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null){
            mPresenter.destroy();
        }
    }
}
