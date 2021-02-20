package com.utflnx.whokonws.ui.result;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.result.ResultRepository;
import com.utflnx.whokonws.ui.result.extension.ResultAdapter;

import java.util.List;

public class ResultFragment extends Fragment implements ResultMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private ResultMainContract.Presenter mPresenter;
    private ResultRepository mResultRepository;
    private final static String KEY_RESULT_FRAGMENT = "KeyResultFragment";
    private View rootView;
    private Context mContext;

    private RoomModel currentRoomModel = null;
    private RecyclerView mRecyclerView;
    private ResultAdapter mAdapter;

    public static ResultFragment createInstance(RoomModel roomModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_RESULT_FRAGMENT, roomModel);

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mResultRepository = new ResultRepository(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ResultPresenter(this, mResultRepository);
        if (getArguments() != null)
            currentRoomModel = (RoomModel) getArguments().getSerializable(KEY_RESULT_FRAGMENT);

        mPresenter.displayResult(currentRoomModel);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_result, container, false);

        initializeLayout();

        return rootView;
    }

    private void initializeLayout() {
        mAdapter = new ResultAdapter(mContext);
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResultDisplay(List<ResultModel> resultModels) {
        Log.d(TAG, "onResultDisplay");

        mAdapter.setData(resultModels);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgressShow");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgressHide");
    }

    @Override
    public void onResultEmpty() {
        Log.d(TAG, "onResultEmpty");
    }

    @Override
    public void onError(Throwable t) {
        String message = "Sorry, "+t.getLocalizedMessage();
        Log.d(TAG, message);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ResultMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
    }
}
