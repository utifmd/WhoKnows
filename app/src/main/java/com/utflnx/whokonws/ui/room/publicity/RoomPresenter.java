package com.utflnx.whokonws.ui.room.publicity;

import android.util.Log;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.participant.ParticipantDataContract;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizDataContract;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomDataContract;
import com.utflnx.whokonws.repo.room.RoomRepository;

import java.util.List;

public class RoomPresenter implements RoomMainContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private RoomMainContract.View mView = null;
    private final RoomRepository mRepository;
    private final ProfileRepository mProfileRepository;
    private final QuizRepository mQuizRepository;
    private final ParticipateRepository mParticipateRepository;

    public RoomPresenter(RoomMainContract.View view, RoomRepository repository, ProfileRepository profileRepository, QuizRepository quizRepository, ParticipateRepository participateRepository) {
        this.mView = view;
        this.mRepository = repository;
        this.mProfileRepository = profileRepository;
        this.mQuizRepository = quizRepository;
        this.mParticipateRepository = participateRepository;

        mView.setPresenter(this);
    }

    @Override
    public void joinRoom(String roomId) {
        if (mView == null) return;

        mView.onProgressShow();
        mRepository.getRemoteRoom(roomId, new RoomDataContract.LoadedRoomCallback() {
            @Override
            public void onRoomLoaded(RoomModel roomModel) {
                filterOnlyCompleteRoom(roomModel);
            }

            @Override
            public void onRoomEmpty() {
                mView.onRoomEmpty();
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
    public void takingRoom(ParticipantModel participantModel) {
        if (mView == null) return;

        mView.onProgressShow();
        mRepository.postPublicParticipant(participantModel, new RoomDataContract.ActionParticipantCallBack() {
            @Override
            public void onRoomResponse(ParticipantModel model) {
                mView.onRoomTakenSaved(model);
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
    public void expireRoom(RoomModel currentRoom) {
        if (mView == null) return;

        mView.onProgressShow();
        mRepository.updateCurrentRoom(currentRoom, new RoomDataContract.ActionRoomCallBack() {
            @Override
            public void onRoomResponse(RoomModel roomModel) {
                mView.onRoomExpired(roomModel);
                mView.onProgressHide();
            }

            @Override
            public void onError(Throwable t) {
                mView.onError(t);
                mView.onProgressHide();
            }
        });
    }

    private void filterOnlyCompleteRoom(RoomModel roomModel) {
        if (mView == null) return;

        mQuizRepository.getRoomQuizList(roomModel, new QuizDataContract.LoadedQuizCallback() {
            @Override
            public void onQuizListLoaded(List<QuizModel> quizModelList) {
                mView.onRoomRemoteLoaded(roomModel);
                //mView.onProgressHide();
            }

            @Override
            public void onQuizListEmpty() {
                mView.onError(new Throwable("currently this room has not complete, please get another room."));
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
    public void saveCurrentRoom(RoomModel roomModel) {
        if (mView == null) return;

        mRepository.postLocalRoom(roomModel);
    }

    @Override
    public void removeCurrentRoom(RoomModel roomModel) {
        if (mView == null) return;

        mQuizRepository.deleteQuizLocal();
    }

    @Override
    public void displayCurrentRoom() {
        if (mView == null) return;

        mView.onProgressShow();
        mRepository.getLocalRoom(new RoomDataContract.LoadedRoomCallback() {
            @Override
            public void onRoomLoaded(RoomModel roomModel) {
                mView.onRoomLocalLoaded(roomModel);
                //mView.onProgressHide();
            }

            @Override
            public void onRoomEmpty() {
                mView.onRoomEmpty();
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
    public void detectParticipation(UserModel currentUser, RoomModel roomModel) {
        if (mView == null) return;

        mParticipateRepository.getCurrentParticipate(currentUser, roomModel, new ParticipantDataContract.LoadedParticipantCallback() {
            @Override
            public void onParticipateExist(ParticipantModel participantModels) {
                mView.onParticipationExist(participantModels);
                mView.onProgressHide();
            }

            @Override
            public void onParticipateExpired() {
                mView.onParticipationExpired();
                mView.onProgressHide();
            }

            @Override
            public void onEmptyParticipant() {
                mView.onParticipantEmpty();
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
    public void displayRoomQuizList(RoomModel roomModel) {
        if (mView == null) return;

        mView.onProgressShow();
        mQuizRepository.getRoomQuizList(roomModel, new QuizDataContract.LoadedQuizCallback() {
            @Override
            public void onQuizListLoaded(List<QuizModel> quizModelList) {
                mView.onQuestionsLoaded(quizModelList);
                mView.onProgressHide();
            }

            @Override
            public void onQuizListEmpty() {
                mView.onQuestionsEmpty();
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
    public void selectItemQuiz(QuizModel quizModel) {
        if (mView == null) return;

        mView.onItemQuizSelected(quizModel);
    }

    @Override
    public void start() { }

    @Override
    public void destroy() {
        mView = null;
    }
}
