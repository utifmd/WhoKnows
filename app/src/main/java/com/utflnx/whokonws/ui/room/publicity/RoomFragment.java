package com.utflnx.whokonws.ui.room.publicity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.MainActivity;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomRepository;
import com.utflnx.whokonws.ui.MainPresenter;
import com.utflnx.whokonws.ui.quiz.QuizFragment;
import com.utflnx.whokonws.ui.result.ResultFragment;
import com.utflnx.whokonws.ui.room.publicity.extension.RoomAdapter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RoomFragment extends Fragment implements RoomMainContract.View, MainPresenter.QuizScopeListener.Callback {
    private final String TAG = getClass().getSimpleName();
    private final static String KEY_FRAGMENT = "RoomFragmentKey";
    private final static String KEY_IS_STATE_BY_OWNER = "IsStateByOwner";
    private RoomMainContract.Presenter mPresenter;
    private MainPresenter.RoomScopeListener.Callback roomCallback;
    private RoomRepository roomRepository;
    private ProfileRepository profileRepository;
    private QuizRepository quizRepository;
    private ParticipateRepository participateRepository;
    private FragmentActivity mContext;
    private View rootView, contentPublicRoom, contentInside, contentJoin, contentList, btnCloseContent, contentEmpty, btnCopyRoomId;//, rootView;
    private TextInputLayout inpRoomId;
    private TextView roomTitle, roomDesc, roomTime;
    private MaterialCardView cardRoom;
    private Button btnJoinRoom, btnOwner, btnTakeRoom;

    private UserModel currentUser = null;
    private RoomModel currentRoomModel = null;
    private ParticipantModel currentParticipantModel = null;
    private int mQuestionSize = 0;
    private boolean isStateByOwner = false;

    private RoomAdapter roomAdapter;
    private RecyclerView recyclerView;

    private final int KEY_ROOM_GENERAL = 0;
    private final int KEY_ROOM_CONTINUE = 1;
    private final int KEY_ROOM_FINISH = 2;

    public static RoomFragment createInstance(UserModel currentUser, RoomModel roomModel, boolean ownRoom){
        RoomFragment roomFragment = new RoomFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        bundle.putSerializable(KEY_FRAGMENT, roomModel);
        bundle.putBoolean(KEY_IS_STATE_BY_OWNER, ownRoom);

        roomFragment.setArguments(bundle);
        return roomFragment;
    }

    public RoomFragment(){
        super(R.layout.fragment_room);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        roomRepository = new RoomRepository(context);
        profileRepository = new ProfileRepository(context);
        quizRepository = new QuizRepository(context);
        participateRepository = new ParticipateRepository(context);
        roomAdapter = new RoomAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = (UserModel) requireArguments().getSerializable(ListObjects.KEY_CURRENT_USER);
        currentRoomModel = (RoomModel) requireArguments().getSerializable(KEY_FRAGMENT);
        isStateByOwner = requireArguments().getBoolean(KEY_IS_STATE_BY_OWNER);

        mContext.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                popToOwnerRoom(rootView);
            }
        });

        new RoomPresenter(this, roomRepository, profileRepository, quizRepository, participateRepository);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rootView = view;

        initializeLayout(view);
        displayRoom();
    }

    @Override
    public void onRoomRemoteLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomRemoteLoaded()");
        try {
            mPresenter.saveCurrentRoom(roomModel);

            if (currentUser != null)
                mPresenter.detectParticipation(currentUser, roomModel);
            else throw new IllegalArgumentException("CurrentUser Invalid");
        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public void onRoomLocalLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomLocalLoaded()");

        if (currentUser != null)
            mPresenter.detectParticipation(currentUser, roomModel);
        else onError(new Throwable("CurrentUser invalid"));
    }

    @Override
    public void onRoomEmpty() {
        Log.d(TAG, "onRoomEmpty()");

        mContext.runOnUiThread(this::displayPublicJoin);
    }

    @Override
    public void onRoomJoinSaved(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomJoinSaved()");
    }

    @Override
    public void onRoomTakenSaved(ParticipantModel participantModel) { currentParticipantModel = participantModel;
        Log.d(TAG, "onRoomTakenSaved()"); //mPresenter.saveCurrentParticipant(participantModel);

        if(currentRoomModel != null) navigateToQuiz(currentRoomModel, participantModel);
    }

    @Override
    public void onRoomExpired(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomExpired");

        roomCallback.onNotify("Successfully publish, share this room to participants by copy the room Id.", Snackbar.LENGTH_LONG);

        popToOwnerRoom(rootView);
    }

    @Override
    public void onQuestionsLoaded(List<QuizModel> quizModelList) {
        Log.d(TAG, "onRoomQuizLoaded");
        mQuestionSize = quizModelList.size();

        mContext.runOnUiThread(() -> { if (isStateByOwner)
            displayOwnerList(quizModelList);
        else {

            displayPublicRoom(KEY_ROOM_CONTINUE);
            setHeaderRoom(currentRoomModel);
        }});
    }

    @Override
    public void onQuestionsEmpty() {
        Log.d(TAG, "onQuestionsEmpty");

        mContext.runOnUiThread(() -> displayOwnerList(Collections.emptyList()));
    }

    private void copyRoomClipboard(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Room Id", currentRoomModel.getRoomId());
        if(currentRoomModel != null) {
            clipboardManager.setPrimaryClip(clipData);
            roomCallback.onNotify("Room id copied.", Snackbar.LENGTH_SHORT);
        }else
            roomCallback.onNotify("Invalid room id.", Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onItemQuizSelected(QuizModel quizModel) {
        Log.d(TAG, "onItemQuizSelected "+ quizModel.getQuizId());
    }

    @Override
    public void onParticipationExist(ParticipantModel participantModels) { currentParticipantModel = participantModels;
        Log.d(TAG, "onExistParticipation");

        roomCallback.onNotify("You already joined the room, please finish the quiz!", Snackbar.LENGTH_LONG);

        mContext.runOnUiThread(() -> {
            setHeaderRoom(currentRoomModel);
            displayPublicRoom(KEY_ROOM_CONTINUE);
        });
    }

    @Override
    public void onParticipationExpired() {
        Log.d(TAG, "onExpiredParticipate");

        roomCallback.onNotify("Thank's for your participation!", Snackbar.LENGTH_SHORT);

        mContext.runOnUiThread(() -> {
            setHeaderRoom(currentRoomModel);
            displayPublicRoom(KEY_ROOM_FINISH);
        });
    }

    @Override
    public void onParticipantEmpty() {
        Log.d(TAG, "onParticipantEmpty");

        roomCallback.onNotify("Welcome to the room, please finish the quiz!", Snackbar.LENGTH_SHORT);

        mContext.runOnUiThread(() -> {
            setHeaderRoom(currentRoomModel);
            displayPublicRoom(KEY_ROOM_GENERAL);
        });
    }


    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
        roomCallback.onNotify("Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgressShow()");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgressHide()");
    }

    private void initializeLayout(View rootView){
        contentInside = rootView.findViewById(R.id.contentInside);
        contentJoin = rootView.findViewById(R.id.contentJoin);
        cardRoom = rootView.findViewById(R.id.card_room);

        contentPublicRoom = rootView.findViewById(R.id.contentPublicRoom);
        contentList = rootView.findViewById(R.id.contentList);
        contentEmpty = rootView.findViewById(R.id.contentEmpty);

        roomTitle = rootView.findViewById(R.id.item_title);
        roomDesc = rootView.findViewById(R.id.room_desc);
        roomTime = rootView.findViewById(R.id.room_time);

        btnCloseContent = rootView.findViewById(R.id.btn_close_content);
        btnJoinRoom = rootView.findViewById(R.id.btn_join_room);
        btnOwner = rootView.findViewById(R.id.btn_add_room_quiz);
        btnCopyRoomId = rootView.findViewById(R.id.btn_copy_room_id);
        btnTakeRoom = rootView.findViewById(R.id.btn_take_room);
        inpRoomId = rootView.findViewById(R.id.text_input_room_id);
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
    }

    private void popToOwnerRoom(View view) {
        roomCallback.onInvalidateToolbar(MainActivity.STATE_OWNER_ROOM);
        ListObjects.fragmentManager(mContext).popBackStack();
    }

    private void displayRoom(){
        if (currentRoomModel != null)
            mPresenter.displayRoomQuizList(currentRoomModel);
        else
            mPresenter.displayCurrentRoom();
    }

    private void joinRoom(View btnView){
        EditText etRoomId = inpRoomId.getEditText();

        if (etRoomId != null){
            if (!etRoomId.getText().toString().trim().isEmpty())
                mPresenter.joinRoom(etRoomId.getText().toString().trim());
            else
                roomCallback.onNotify(getString(R.string.form_cant_empty), Snackbar.LENGTH_SHORT);
        }else
            roomCallback.onNotify(getString(R.string.invalid_input), Snackbar.LENGTH_SHORT);
    }

    private void setHeaderRoom(RoomModel roomModel) {
        roomTitle.setText(roomModel.getTitle());
        roomDesc.setText(roomModel.getDesc());
        roomTime.setText(roomModel.getMinute()+" minutes");

        if (currentRoomModel.isExpired()){
            btnOwner.setText(R.string.result);
            btnOwner.setOnClickListener(view -> this.resultQuiz(currentRoomModel));
            btnCopyRoomId.setVisibility(View.VISIBLE);
            btnCopyRoomId.setOnClickListener(this::copyRoomClipboard);
        }else {
            btnCopyRoomId.setVisibility(View.GONE);
            btnOwner.setOnClickListener(this::createQuiz);
            cardRoom.setOnClickListener(view -> cardRoom.setChecked(!cardRoom.isChecked()));
            cardRoom.setOnCheckedChangeListener((card, isChecked) -> {
                if (isChecked) {
                    btnOwner.setText(R.string.publish);
                    btnOwner.setOnClickListener(this::publishQuiz);
                }else {
                    btnOwner.setText(R.string.new_question);
                    btnOwner.setOnClickListener(this::createQuiz);
                }
            });
        }
    }

    private void displayPublicJoin(){
        ListObjects.visibleGoneView(new View[]{contentJoin}, contentInside, contentPublicRoom, contentList);

        btnJoinRoom.setOnClickListener(this::joinRoom);
    }

    private void displayPublicRoom(int state){
        ListObjects.visibleGoneView(new View[]{contentInside, contentPublicRoom}, contentJoin, contentList);

        switch (state){
            case KEY_ROOM_GENERAL:
                btnTakeRoom.setEnabled(true);
                btnTakeRoom.setOnClickListener(this::takeTheRoom);
                break;
            case KEY_ROOM_CONTINUE:
                btnTakeRoom.setEnabled(true);
                btnTakeRoom.setText(R.string._continue);
                btnTakeRoom.setOnClickListener(v -> navigateToQuiz(currentRoomModel, currentParticipantModel));
                break;
            case KEY_ROOM_FINISH:
                btnTakeRoom.setEnabled(false); //btnTakeRoom.setOnClickListener(this::navigateToQuiz);
                btnTakeRoom.setText(R.string.finish);
                break;
        }

        btnCloseContent.setOnClickListener(this::signOutOverRoom);
    }

    private void displayOwnerList(List<QuizModel> quizModelList){
        if (quizModelList.isEmpty()){
            ListObjects.visibleGoneView(new View[]{contentInside, contentList, contentEmpty}, recyclerView, contentPublicRoom, contentJoin, contentEmpty);

            setHeaderRoom(currentRoomModel);
        }else {
            ListObjects.visibleGoneView(new View[]{contentInside, contentList, recyclerView}, contentEmpty, contentPublicRoom, contentJoin, contentEmpty);

            setHeaderRoom(currentRoomModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            roomAdapter.setData(quizModelList);
            roomAdapter.setPresenter(mPresenter);
            recyclerView.setAdapter(roomAdapter);
        }
    }

    private void createQuiz(View view) {
        QuizFragment fragment = QuizFragment.createInstance(ListObjects.KEY_CREATE_ROOM, currentRoomModel, null);
        fragment.setQuizCallback(this);
        ListObjects.navigateTo(mContext, fragment).addToBackStack(null).commit();
    }

    private void publishQuiz(View view){
        if (currentRoomModel != null) {
            if (mQuestionSize >= 2) {
                currentRoomModel.setExpire(true);
                mPresenter.expireRoom(currentRoomModel);
            } else onError(new Throwable("The room at least has 2 questions."));
        }else onError(new Throwable("Invalid current room."));
    }

    private void resultQuiz(RoomModel roomModel) {
        Fragment fragment = ResultFragment.createInstance(roomModel);
        ListObjects.navigateTo(mContext, fragment).addToBackStack(null).commit();
    }

    private void takeTheRoom(View view) {
        if (currentRoomModel != null){
            ParticipantModel model = new ParticipantModel();
            model.setParticipantId(UUID.randomUUID().toString());
            model.setRoomId(currentRoomModel.getRoomId());
            model.setUserId(currentUser.getUserId());
            model.setTotalQuiz(mQuestionSize);
            model.setCurrentPage(0);
            model.setTotalTime(Integer.parseInt(currentRoomModel.getMinute()));
            model.setTimeRemaining(Integer.parseInt(currentRoomModel.getMinute()));
            model.setExpire(false);

            mPresenter.takingRoom(model);
        } else
            onError(new Throwable("invalid current room."));
    }

    private void navigateToQuiz(RoomModel roomModel, ParticipantModel participantModel){
        QuizFragment fragment = QuizFragment.createInstance(ListObjects.KEY_TAKE_ROOM, roomModel, participantModel);
        fragment.setQuizCallback(this);
        ListObjects.navigateTo(mContext, fragment).addToBackStack(null).commit();
    }

    private void signOutOverRoom(View view) {
        mPresenter.removeCurrentRoom(currentRoomModel);
        displayPublicJoin();
    }

    public void setRoomCallback(MainPresenter.RoomScopeListener.Callback callback){
        roomCallback = callback;
    }

    @Override
    public void setPresenter(RoomMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() { Log.d(TAG, "onDestroy");
        super.onDestroy();
        if(mPresenter != null){ Log.d(TAG, "mPresenter not null");
            mPresenter.destroy();
        }
        if(rootView != null){ Log.d(TAG, "rootView not null");
            rootView = null;
        }
        if (roomCallback != null){
            roomCallback = null;
        }
    }

    @Override
    public void onDisplaySingleTab(int stateOfTab, UserModel currentUser) {
        roomCallback.onDisplaySingleTab(stateOfTab, currentUser);
    }

    @Override
    public void onInvalidateToolbar(int stated) {
        roomCallback.onInvalidateToolbar(stated);
    }

    @Override
    public void onInactivateToolbar() {
        roomCallback.onInactivateToolbar();
    }

    @Override
    public void onNotify(String message, int duration) {
        roomCallback.onNotify(message, duration);
    }
}
