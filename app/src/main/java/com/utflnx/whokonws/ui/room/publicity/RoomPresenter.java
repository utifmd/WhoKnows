package com.utflnx.whokonws.ui.room.publicity;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.participant.ParticipantDataContract;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.quiz.QuizDataContract;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomDataContract;
import com.utflnx.whokonws.repo.room.RoomRepository;

import java.util.List;

public class RoomPresenter implements RoomMainContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private RoomMainContract.View mView;
    private final RoomRepository mRepository;
    private final QuizRepository mQuizRepository;
    private final ParticipateRepository mParticipateRepository;

    public RoomPresenter(RoomMainContract.View mView, RoomRepository repository, QuizRepository quizRepository, ParticipateRepository participateRepository) {
        this.mView = mView;
        this.mRepository = repository;
        this.mQuizRepository = quizRepository;
        this.mParticipateRepository = participateRepository;

        mView.setPresenter(this);
    }

    @Override
    public void joinRoom(String roomId) {
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

    private void filterOnlyCompleteRoom(RoomModel roomModel) {
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
        mRepository.postLocalRoom(roomModel);
    }

    @Override
    public void removeCurrentRoom(RoomModel roomModel) {
        mQuizRepository.deleteQuizLocal();
    }

    @Override
    public void displayCurrentRoom() {
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
    public void displayCurrentParticipate(RoomModel roomModel) {
        mParticipateRepository.getCurrentParticipate(roomModel, new ParticipantDataContract.LoadedParticipantCallback() {
            @Override
            public void onParticipateExist(ParticipantModel participantModels) {
                mView.onExistParticipation(participantModels);
                mView.onProgressHide();
            }

            @Override
            public void onParticipateExpired() {
                mView.onExpiredParticipate();
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
        mView.onItemQuizSelected(quizModel);
    }

    @Override
    public void start() {
        // displayCurrentRoom();
        // displayCurrentParticipate();
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
