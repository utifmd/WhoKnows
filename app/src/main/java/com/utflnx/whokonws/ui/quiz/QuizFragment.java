package com.utflnx.whokonws.ui.quiz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.ResultModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.utflnx.whokonws.api.utils.ListObjects.KEY_CREATE_ROOM;
import static com.utflnx.whokonws.api.utils.ListObjects.KEY_TAKE_ROOM;

public class QuizFragment extends Fragment implements QuizMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private final static String KEY_ROOM_FRAGMENT = "QuizRoomFragmentKey";
    private final static String KEY_PARTICIPANT_FRAGMENT = "QuizParticipantFragmentKey";
    private final static String KEY_TYPE_FRAGMENT = "QuizFragmentTypeKey";
    private FragmentActivity mContext;
    private QuizMainContract.Presenter mPresenter;
    private QuizRepository mRepository;
    private ParticipateRepository mParticipateRepository;
    private ProfileRepository mProfileRepository;
    private View rootView, contentCreate, contentTake;
    private TextView textQuestion;
    private MaterialCheckBox checkOptA, checkOptB, checkOptC, checkOptD, checkOptE;
    private Button btnNext;
    private TextInputLayout inpQuestion, inpOptA, inpOptB, inpOptC, inpOptD, inpOptE;


    private UserModel currentUser = null;
    private RoomModel currentRoomModel = null;
    private ParticipantModel currentParticipantModel = null;
    private List<QuizModel> currentQuizModelList = null;
    private String creatorSelectedAnswer;
    private int mViewType;
    private int viewPageSum = 0;
    private int viewPage = 0;
    private List<String> correctAnswer = new ArrayList<>();
    private List<String> wrongAnswer = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        mRepository = new QuizRepository(context);
        mParticipateRepository = new ParticipateRepository(context);
        mProfileRepository = new ProfileRepository(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new QuizPresenter(this, mRepository, mParticipateRepository, mProfileRepository);

        if (getArguments() != null) {
            Serializable roomModel = getArguments().getSerializable(KEY_ROOM_FRAGMENT);
            Serializable participantModel = getArguments().getSerializable(KEY_PARTICIPANT_FRAGMENT);

            if (roomModel instanceof RoomModel){ Log.d(TAG, "room model called.");
                currentRoomModel = (RoomModel) roomModel;
            }

            if (participantModel instanceof ParticipantModel){ Log.d(TAG, "participant called.");
                currentParticipantModel = (ParticipantModel) participantModel;
            }


            mViewType = getArguments().getInt(KEY_TYPE_FRAGMENT);
        }
    }

    public static QuizFragment createInstance(int key, RoomModel roomModel, ParticipantModel participantModel){
        QuizFragment fragment = new QuizFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(KEY_TYPE_FRAGMENT, key);
        bundle.putSerializable(KEY_ROOM_FRAGMENT, roomModel);
        if(participantModel != null) bundle.putSerializable(KEY_PARTICIPANT_FRAGMENT, participantModel);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        initializeLayout(rootView);
        displayQuiz();

        return rootView;
    }

    private void initializeLayout(View rootView) {
        contentCreate = rootView.findViewById(R.id.content_submit);
        contentTake = rootView.findViewById(R.id.content_view);
        inpQuestion = rootView.findViewById(R.id.text_input_question);
        inpOptA = rootView.findViewById(R.id.text_input_opt_a);
        inpOptB = rootView.findViewById(R.id.text_input_opt_b);
        inpOptC = rootView.findViewById(R.id.text_input_opt_c);
        inpOptD = rootView.findViewById(R.id.text_input_opt_d);
        inpOptE = rootView.findViewById(R.id.text_input_opt_e);

        textQuestion = rootView.findViewById(R.id.textQuestion);
        checkOptA = rootView.findViewById(R.id.check_a);
        checkOptB = rootView.findViewById(R.id.check_b);
        checkOptC = rootView.findViewById(R.id.check_c);
        checkOptD = rootView.findViewById(R.id.check_d);
        checkOptE = rootView.findViewById(R.id.check_e);
        btnNext = rootView.findViewById(R.id.btn_next);

        MaterialButtonToggleGroup toggleOptions = rootView.findViewById(R.id.toggleOptionsQuiz);
        Button btnCreateQuiz = rootView.findViewById(R.id.btn_create_quiz);

        toggleOptions.addOnButtonCheckedListener(this::onOptionsChanged);
        btnCreateQuiz.setOnClickListener(this::createOwnerQuiz);
    }

    private void displayQuiz() {
        switch (mViewType) {
            case KEY_CREATE_ROOM:
                setUpCreateRoom();
                break;
            case KEY_TAKE_ROOM:
                setUpTakeRoom();
                break;
        }
    }

    private void setUpCreateRoom() {
        ListObjects.visibleGoneView(new View[]{contentCreate}, contentTake);
    }

    private void setUpTakeRoom() {
        ListObjects.visibleGoneView(new View[]{contentTake}, contentCreate);
        if (currentRoomModel.getRoomId() != null) mPresenter.getRoomQuizList(currentRoomModel);

        recursiveCheckBoxes(0);
    }

    private void clearSetup(View view){
        ListObjects.visibleGoneView(new View[]{}, contentTake, contentCreate);

        ListObjects.fragmentManager(mContext).popBackStack();
    }

    private void onOptionsChanged(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        creatorSelectedAnswer = ((Button)rootView.findViewById(checkedId)).getText().toString(); // Log.d(TAG, "button with id " +selectedAnswer);
    }

    private void createOwnerQuiz(View view) {
        EditText etQuestion = inpQuestion.getEditText(), etOptA = inpOptA.getEditText(), etOptB = inpOptB.getEditText(), etOptC = inpOptC.getEditText(), etOptD = inpOptD.getEditText(), etOptE = inpOptE.getEditText();
        QuizModel mQuizModel = new QuizModel();
        if (creatorSelectedAnswer != null && etQuestion != null && etOptA != null && etOptB != null && etOptC != null && etOptD != null && etOptE != null){
            if (!creatorSelectedAnswer.isEmpty() && !etQuestion.getText().toString().isEmpty() && !etOptA.getText().toString().isEmpty() && !etOptB.getText().toString().isEmpty() && !etOptC.getText().toString().isEmpty() && !etOptD.getText().toString().isEmpty() && !etOptE.getText().toString().isEmpty()){
                mQuizModel.setQuizId(UUID.randomUUID().toString());
                mQuizModel.setRoomId(currentRoomModel.getRoomId());
                mQuizModel.setImageUrl("http://image_url.com/image1.png");
                mQuizModel.setQuestion(etQuestion.getText().toString().trim());
                mQuizModel.setOptA(etOptA.getText().toString().trim());
                mQuizModel.setOptB(etOptB.getText().toString().trim());
                mQuizModel.setOptC(etOptC.getText().toString().trim());
                mQuizModel.setOptD(etOptD.getText().toString().trim());
                mQuizModel.setOptE(etOptE.getText().toString().trim());
                mQuizModel.setAnswer(creatorSelectedAnswer);

                mPresenter.createOwnerQuiz(mQuizModel);
            }else
                Snackbar.make(rootView, R.string.form_cant_empty, Snackbar.LENGTH_SHORT).show();
        }else
            Snackbar.make(rootView, "Invalid input, please check the answer", Snackbar.LENGTH_SHORT).show();
    }

    private void setUpTakenQuiz(List<QuizModel> quizModelList) { Log.d(TAG, "setUpTakenQuiz: "+viewPage+"/"+viewPageSum);
        textQuestion.setText(quizModelList.get(viewPage).getQuestion());
        checkOptA.setText(quizModelList.get(viewPage).getOptA());
        checkOptB.setText(quizModelList.get(viewPage).getOptB());
        checkOptC.setText(quizModelList.get(viewPage).getOptC());
        checkOptD.setText(quizModelList.get(viewPage).getOptD());
        checkOptE.setText(quizModelList.get(viewPage).getOptE());

        if (viewPage != viewPageSum){
            btnNext.setOnClickListener(view -> {
                try {
                    viewPage++;
                    recursiveCheckBoxes(0);
                    setUpTakenQuiz(quizModelList);
                } catch (Throwable t) {
                    onError(t);
                }
            });
        }else {
            btnNext.setText(R.string.finish);
            btnNext.setOnClickListener(view -> saveTakenQuiz());
        }
    }

    private void recursiveCheckBoxes(int checked) { //6fcb5d7e-a85b-40c5-894c-874977982c54
        MaterialCheckBox[] mCheckBoxes = new MaterialCheckBox[]{checkOptA, checkOptB, checkOptC, checkOptD, checkOptE};
        String[] options = new String[]{"A", "B", "C", "D", "E"};

        for (int i = 0; i < mCheckBoxes.length; i++) {
            int index = i;
            mCheckBoxes[i].setChecked(mCheckBoxes[i] == mCheckBoxes[checked]);

            mCheckBoxes[i].setOnClickListener(view -> {
                recursiveCheckBoxes(index);
                if (currentQuizModelList.get(viewPage).getAnswer().equals(options[index]))
                    correctAnswer.add(currentQuizModelList.get(viewPage).getQuestion()); // Log.d(TAG, options[index]+" is correct answer");
                else wrongAnswer.add(currentQuizModelList.get(viewPage).getQuestion()); // Log.d(TAG, options[index]+" is wrong answer");

            });
        }
    }

    private void saveTakenQuiz() {
        if (currentParticipantModel != null && currentUser != null && currentRoomModel != null){
            ResultModel resultModel = new ResultModel();
            resultModel.setResultId(UUID.randomUUID().toString());
            resultModel.setRoomId(currentParticipantModel.getRoomId());
            resultModel.setUserId(currentParticipantModel.getUserId());
            resultModel.setUserName(currentUser.getFullName());
            resultModel.setCorrectQuiz(correctAnswer.toString());
            resultModel.setWrongQuiz(wrongAnswer.toString());
            resultModel.setScore(correctAnswer.size()+" * 10 / "+currentQuizModelList.size()+ " = "+ correctAnswer.size() * 10 / currentQuizModelList.size());

            mPresenter.expireParticipant(currentParticipantModel, resultModel); //clearSetup(view);

        }else onError(new Throwable("Current data is not available."));
    }

    @Override
    public void onCurrentUserLoaded(UserModel userModel) { currentUser = userModel;
        Log.d(TAG, "onCurrentUserLoaded");
    }

    @Override
    public void onCurrentUserEmpty() {
        Log.d(TAG, "onCurrentUserEmpty");
    }

    @Override
    public void onRoomQuizListLoaded(List<QuizModel> quizModelList) { currentQuizModelList = quizModelList;
        Log.d(TAG, "onRoomQuizListLoaded");
        viewPageSum = quizModelList.size()-1;

        setUpTakenQuiz(quizModelList);
    }

    @Override
    public void onQuizDisplay() {
        Log.d(TAG, "onQuizDisplay");
    }
    @Override
    public void onQuizEmpty() {
        Log.d(TAG, "onQuizEmpty");
    }

    @Override
    public void onOwnerQuizRemoteSaved(QuizModel quizModel) {
        Log.d(TAG, "onQuizRemoteSaved");

        clearSetup(rootView);
    }

    @Override
    public void onExpiredParticipantTakenQuiz(ParticipantModel participantModel, ResultModel resultModel) { // participant already updated.
        Log.d(TAG, "onFinishParticipantTakenQuiz");

        ListObjects.navigateTo(mContext, new RoomOwnerFragment(), false).commit();
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "onError "+ t.getLocalizedMessage());
        Snackbar.make(rootView, "Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void setPresenter(QuizMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
