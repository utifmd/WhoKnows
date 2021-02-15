package com.utflnx.whokonws.ui.quiz;

import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.participant.ParticipantDataContract;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.quiz.QuizDataContract;
import com.utflnx.whokonws.repo.quiz.QuizRepository;

import java.util.List;

public class QuizPresenter implements QuizMainContract.Presenter {
    private QuizMainContract.View mView;
    private final QuizRepository quizRepository;
    private final ParticipateRepository participateRepository;

    public QuizPresenter(QuizMainContract.View view, QuizRepository quizRepository, ParticipateRepository participateRepository) {
        this.mView = view;
        this.quizRepository = quizRepository;
        this.participateRepository = participateRepository;

        mView.setPresenter(this);
    }

    @Override
    public void createOwnerQuiz(QuizModel quizModel) {
        mView.onProgressShow();
        quizRepository.postQuizRemote(quizModel, new QuizDataContract.ActionQuizCallback() {
            @Override
            public void onQuizResponse(QuizModel quizModel) {
                mView.onOwnerQuizRemoteSaved(quizModel);
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
    public void getRoomQuizList(RoomModel roomModel) {
        mView.onProgressShow();
        quizRepository.getRoomQuizList(roomModel, new QuizDataContract.LoadedQuizCallback() {
            @Override
            public void onQuizListLoaded(List<QuizModel> quizModelList) {
                mView.onRoomQuizListLoaded(quizModelList);
                mView.onProgressHide();
            }

            @Override
            public void onQuizListEmpty() {
                mView.onQuizEmpty();
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
    public void finishTakenQuiz(ParticipantModel participantModel) {
        mView.onProgressShow();
        participateRepository.updateCurrentParticipant(participantModel, new ParticipantDataContract.ActionParticipantCallback() {
            @Override
            public void onParticipantResponse(ParticipantModel participantModel) {
                mView.onFinishParticipantTakenQuiz(participantModel);
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
