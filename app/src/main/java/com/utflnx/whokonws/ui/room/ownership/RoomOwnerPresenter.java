package com.utflnx.whokonws.ui.room.ownership;

import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomDataContract;
import com.utflnx.whokonws.repo.room.RoomRepository;

import java.util.List;

public class RoomOwnerPresenter implements RoomOwnerMainContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private RoomOwnerMainContract.View mView;
    private final RoomRepository mRepository;
    private final ProfileRepository mProfileRepository;
    private final QuizRepository mQuizRepository;

    public RoomOwnerPresenter(RoomOwnerMainContract.View mView, RoomRepository repository, ProfileRepository profileRepository, QuizRepository quizRepository) {
        this.mView = mView;
        this.mRepository = repository;
        this.mProfileRepository = profileRepository;
        this.mQuizRepository = quizRepository;

        mView.setPresenter(this);
    }

    @Override
    public void displayOwnerRoom(UserModel currentUserModel) {
        mView.onProgressShow();
        mRepository.getOwnerRoom(currentUserModel, new RoomDataContract.LoadedRoomListCallback() {
            @Override
            public void onOwnerRoomLoaded(List<RoomModel> roomModels) {
                mView.onRoomOwnerLoaded(roomModels);
                mView.onProgressHide();
            }

            @Override
            public void onOwnerRoomEmpty() {
                mView.onRoomOwnerEmpty();
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
    public void createRoom(RoomModel roomModel) {
        mView.onProgressShow();
        mRepository.postOwnerRoom(roomModel, new RoomDataContract.ActionRoomCallBack() {
            @Override
            public void onRoomResponse(RoomModel roomModel) {
                mView.onRoomOwnerSaved(roomModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable t) {
                mView.onError(t);
                mView.onProgressHide();
            }
        });
    }

    private void getRemoteRoomBy(String roomId) {
        mRepository.getRemoteRoom(roomId, new RoomDataContract.LoadedRoomCallback() {
            @Override
            public void onRoomLoaded(RoomModel roomModel) {
                mView.onRoomLoaded(roomModel);
                mView.onProgressHide();
            }

            @Override
            public void onRoomEmpty() {
                mView.onRoomOwnerEmpty();
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
    public void removeRoom(RoomModel roomModel) {
        mView.onProgressShow();
        mRepository.deleteOwnerRoom(roomModel, new RoomDataContract.ActionRoomCallBack() {
            @Override
            public void onRoomResponse(RoomModel roomModel) {
                mView.onRoomDeleted(roomModel);
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
    public void selectRoomItem(RoomModel roomModel) {
        mView.onRoomItemSelected(roomModel);
    }

    @Override
    public boolean selectLongRoomItem(RoomModel roomModel, int pos) {
        return mView.onRoomItemLongSelected(roomModel, pos);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
