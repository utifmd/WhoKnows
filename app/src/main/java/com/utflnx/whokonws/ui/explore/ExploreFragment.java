package com.utflnx.whokonws.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.ExploreModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.explore.ExploreRepository;
import com.utflnx.whokonws.ui.explore.extension.ExploreAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExploreFragment extends Fragment implements ExploreMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private ExploreMainContract.Presenter mPresenter;
    private ExploreRepository mRepository;
    private FragmentActivity mContext;
    private View rootView;

    private UserModel mCurrentUser = null;

    private RecyclerView mRecyclerView;
    private ExploreAdapter mExploreAdapter;
    private View mContentEmpty;

    @Override
    public void setPresenter(ExploreMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        mRepository = new ExploreRepository(context);
    }

    public static ExploreFragment createInstance(UserModel currentUser){
        ExploreFragment fragment = new ExploreFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(bundle);
        return fragment;
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
        rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        initializeLayout();

        return rootView;
    }

    private void initializeLayout() {
        mExploreAdapter = new ExploreAdapter();
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);

        mContentEmpty = rootView.findViewById(R.id.contentEmpty);
    }

    @Override
    public void onExploreLoaded(List<ExploreModel> exploreModels) {
        Log.d(TAG, "onExploreLoaded");
        mContext.runOnUiThread(()-> replaceWithList(exploreModels));
    }

    @Override
    public void onExploreEmpty() {
        Log.d(TAG, "onExploreEmpty");
        mContext.runOnUiThread(this::replaceWithEmpty);
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
    private void replaceWithList(List<ExploreModel> exploreModels) {
        mExploreAdapter.setData(exploreModels);
        mRecyclerView.setAdapter(mExploreAdapter);
    }

    private void replaceWithEmpty() {
        ListObjects.visibleGoneView(new View[]{mContentEmpty}, mRecyclerView);
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
