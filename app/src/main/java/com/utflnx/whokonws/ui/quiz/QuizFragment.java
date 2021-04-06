package com.utflnx.whokonws.ui.quiz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.utflnx.whokonws.MainActivity;
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
import com.utflnx.whokonws.ui.MainPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.utflnx.whokonws.api.utils.ListObjects.KEY_CREATE_ROOM;
import static com.utflnx.whokonws.api.utils.ListObjects.KEY_TAKE_ROOM;

// TODO: 04/03/21
/*
1. issue on finish taken quiz should to ownRoom
2. issue snack bar have to show in main_activity.xmllayout
* */

public class QuizFragment extends Fragment implements QuizMainContract.View{
    private final String TAG = getClass().getSimpleName();
    private final static String KEY_ROOM_FRAGMENT = "QuizRoomFragmentKey";
    private final static String KEY_PARTICIPANT_FRAGMENT = "QuizParticipantFragmentKey";
    private final static String KEY_TYPE_FRAGMENT = "QuizFragmentTypeKey";
    private FragmentActivity mContext;
    private QuizMainContract.Presenter mPresenter;
    private QuizRepository mRepository;
    private MainPresenter.QuizScopeListener.Callback mQuizCallback;
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
    private List<QuizModel> currentQuizModelList = new ArrayList<>();
    private String creatorSelectedAnswer;
    private int mViewType;
    private int viewPageSum = 0;
    private int viewPage = 0;
    private boolean isOptionsDecided = false;
    private final ArrayList<String> correctAnswer = new ArrayList<>();
    private final ArrayList<String> wrongAnswer = new ArrayList<>();
    private HashMap<String, String> answers = new HashMap<>();

    public static QuizFragment createInstance(int key, RoomModel roomModel, ParticipantModel participantModel){
        QuizFragment fragment = new QuizFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(KEY_TYPE_FRAGMENT, key);
        bundle.putSerializable(KEY_ROOM_FRAGMENT, roomModel);
        if(participantModel != null) bundle.putSerializable(KEY_PARTICIPANT_FRAGMENT, participantModel);

        fragment.setArguments(bundle);

        return fragment;
    }

    public QuizFragment(){
        super(R.layout.fragment_quiz);
    }

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

        currentRoomModel = (RoomModel) requireArguments().getSerializable(KEY_ROOM_FRAGMENT); // if (roomModel instanceof RoomModel)
        currentParticipantModel = (ParticipantModel) requireArguments().getSerializable(KEY_PARTICIPANT_FRAGMENT);
        mViewType = requireArguments().getInt(KEY_TYPE_FRAGMENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rootView = view;

        initializeLayout(rootView);
        displayQuiz();

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
        Log.d(TAG, "begin correctAnswer.size() "+correctAnswer.size());
        Log.d(TAG, "begin wrong.size() "+wrongAnswer.size());
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
        if (creatorSelectedAnswer != null && etQuestion != null && etOptA != null && etOptB != null && etOptC != null && etOptD != null && etOptE != null){
            if (!creatorSelectedAnswer.isEmpty() && !etQuestion.getText().toString().isEmpty() && !etOptA.getText().toString().isEmpty() && !etOptB.getText().toString().isEmpty() && !etOptC.getText().toString().isEmpty() && !etOptD.getText().toString().isEmpty() && !etOptE.getText().toString().isEmpty()){

                QuizModel mQuizModel = new QuizModel();
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
                mQuizCallback.onNotify(getString(R.string.form_cant_empty), Snackbar.LENGTH_SHORT);
        }else
            mQuizCallback.onNotify("Invalid input, please check the answer", Snackbar.LENGTH_SHORT);
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
                if (isOptionsDecided){
                    try {
                        viewPage++;
                        isOptionsDecided = false;
                        recursiveCheckBoxes(0);
                        setUpTakenQuiz(quizModelList);
                        decisionMaking();
                    } catch (Throwable t) {
                        onError(t);
                    }
                }else onError(new Throwable("Please select an answer of the quiz."));
            });
        }else {
            btnNext.setText(R.string.finish);
            btnNext.setOnClickListener(view -> {
                if (isOptionsDecided) {
                    try {
                        decisionMaking();
                        saveTakenQuiz();
                    }catch (Exception e){
                        onError(e);
                    }
                }
                else onError(new Throwable("Please select an answer of the quiz."));
            });
        }
    }

    private void decisionMaking() {
        if(answers.get("correct") != null) { Log.d(TAG, "add correct answer");
            correctAnswer.add(answers.get("correct"));
        }
        if(answers.get("wrong") != null) { Log.d(TAG, "add wrong answer");
            wrongAnswer.add(answers.get("wrong"));
        }
    }

    private void recursiveCheckBoxes(int checked) { //6fcb5d7e-a85b-40c5-894c-874977982c54
        MaterialCheckBox[] mCheckBoxes = new MaterialCheckBox[]{checkOptA, checkOptB, checkOptC, checkOptD, checkOptE};
        String[] options = new String[]{"A", "B", "C", "D", "E"};

        for (int i = 0; i < mCheckBoxes.length; i++) {
            int index = i;
            if (isOptionsDecided) mCheckBoxes[i].setChecked(mCheckBoxes[i] == mCheckBoxes[checked]);
            else mCheckBoxes[i].setChecked(false);

            mCheckBoxes[i].setOnClickListener(view -> { isOptionsDecided = true;
                recursiveCheckBoxes(index);
                if (currentQuizModelList.get(viewPage).getAnswer().equals(options[index])) {
                    answers.clear();
                    answers.put("correct", currentQuizModelList.get(viewPage).getQuestion()+" = [correct] "+options[index]);
                }else {
                    answers.clear();
                    answers.put("wrong", currentQuizModelList.get(viewPage).getQuestion()+" = [wrong] "+options[index]);
                }
            });
        }
    }

    private void saveTakenQuiz() {
        if (currentParticipantModel != null && currentUser != null && currentRoomModel != null){
            ResultModel resultModel = new ResultModel();
            resultModel.setResultId(UUID.randomUUID().toString());
            resultModel.setRoomId(currentParticipantModel.getRoomId());
            resultModel.setUserId(currentUser.getUserId());
            resultModel.setUserName(currentUser.getFullName());
            resultModel.setCorrectQuiz(correctAnswer.toString());
            resultModel.setWrongQuiz(wrongAnswer.toString());
            resultModel.setScore(correctAnswer.size()+" * 10 / "+currentQuizModelList.size()+ " = "+ correctAnswer.size() * 10 / currentQuizModelList.size());

            Log.d(TAG, resultModel.toString());
            // mPresenter.expireParticipant(currentParticipantModel, resultModel); //clearSetup(view);

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

        mContext.runOnUiThread(()-> setUpTakenQuiz(quizModelList));
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

        mContext.runOnUiThread(() -> clearSetup(rootView));
    }

    @Override
    public void onFinishParticipantTakenQuiz(ParticipantModel participantModel, ResultModel resultModel) { // participant already updated.
        Log.d(TAG, "onFinishParticipantTakenQuiz");

        mQuizCallback.onDisplaySingleTab(MainActivity.STATE_OWNER_ROOM, currentUser);
        mQuizCallback.onNotify("Thank you for your participation "+currentUser.getFullName()+"!, hope you enjoy the quiz :)", Snackbar.LENGTH_LONG);
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "onError "+ t.getLocalizedMessage());
        mQuizCallback.onNotify("Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
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
        if (mQuizCallback != null){
            mQuizCallback = null;
        }
    }

    public void setQuizCallback(MainPresenter.QuizScopeListener.Callback quizCallback) {
        mQuizCallback = quizCallback;
    }

    @Override
    public void setPresenter(QuizMainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
