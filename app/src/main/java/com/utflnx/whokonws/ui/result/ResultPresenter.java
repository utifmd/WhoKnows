package com.utflnx.whokonws.ui.result;

import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.result.ResultDataContract;
import com.utflnx.whokonws.repo.result.ResultRepository;

import java.util.List;

public class ResultPresenter implements ResultMainContract.Presenter {
    private ResultMainContract.View mView;
    private final ResultRepository mResultRepository;

    public ResultPresenter(ResultMainContract.View view, ResultRepository resultRepository) {
        this.mView = view;
        this.mResultRepository = resultRepository;

        view.setPresenter(this);
    }

    @Override
    public void displayResult(RoomModel roomModel) {
        mView.onProgressShow();
        mResultRepository.getResultBy(roomModel, new ResultDataContract.LoadedResultCallback() {
            @Override
            public void onResultLoaded(List<ResultModel> resultModels) {
                mView.onResultDisplay(resultModels);
                mView.onProgressHide();
            }

            @Override
            public void onResultEmpty() {
                mView.onResultEmpty();
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

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
