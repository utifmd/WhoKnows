package com.utflnx.whokonws.ui.room.publicity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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
import com.utflnx.whokonws.ui.quiz.QuizFragment;
import com.utflnx.whokonws.ui.result.ResultFragment;
import com.utflnx.whokonws.ui.room.publicity.extension.RoomAdapter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RoomFragment extends Fragment implements RoomMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private final static String KEY_FRAGMENT = "RoomFragmentKey";
    private final static String KEY_IS_STATE_BY_OWNER = "IsStateByOwner";
    private RoomMainContract.Presenter mPresenter;
    private RoomRepository roomRepository;
    private ProfileRepository profileRepository;
    private QuizRepository quizRepository;
    private ParticipateRepository participateRepository;
    private FragmentActivity mContext;
    private View rootView, contentPublicRoom, contentJoin, contentList, btnCloseContent, contentEmpty, btnCopyRoomId;
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

    public static RoomFragment createInstance(UserModel currentUser, RoomModel roomModel, boolean isByOwner){
        RoomFragment roomFragment = new RoomFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        bundle.putSerializable(KEY_FRAGMENT, roomModel);
        bundle.putBoolean(KEY_IS_STATE_BY_OWNER, isByOwner);

        roomFragment.setArguments(bundle);
        return roomFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentUser = (UserModel) getArguments().getSerializable(ListObjects.KEY_CURRENT_USER);
            currentRoomModel = (RoomModel) getArguments().getSerializable(KEY_FRAGMENT);
            isStateByOwner = getArguments().getBoolean(KEY_IS_STATE_BY_OWNER);
        }

        Log.d(TAG, "onCreate user: "+currentUser.getEmail());

        new RoomPresenter(this, roomRepository, profileRepository, quizRepository, participateRepository);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_room, container, false);

        initializeLayout(rootView);
        displayRoom();

        return rootView;
    }

    private void joinRoom(View btnView){
        EditText etRoomId = inpRoomId.getEditText();

        if (etRoomId != null){
            if (!etRoomId.getText().toString().trim().isEmpty())
                mPresenter.joinRoom(etRoomId.getText().toString().trim());
            else
                Snackbar.make(rootView, R.string.form_cant_empty, Snackbar.LENGTH_SHORT).show();
        }else
            Snackbar.make(rootView, R.string.invalid_input, Snackbar.LENGTH_SHORT).show();
    }

    private void initializeLayout(View rootView){
        cardRoom = rootView.findViewById(R.id.card_room);
        contentPublicRoom = rootView.findViewById(R.id.contentPublicRoom);
        contentJoin = rootView.findViewById(R.id.contentJoin);
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
        ListObjects.visibleGoneView(new View[]{contentJoin}, contentPublicRoom, contentList);

        btnJoinRoom.setOnClickListener(this::joinRoom);
    }

    private void displayPublicRoom(int state){
        ListObjects.visibleGoneView(new View[]{contentPublicRoom}, contentJoin, contentList);

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
            ListObjects.visibleGoneView(new View[]{contentList, contentEmpty}, recyclerView, contentPublicRoom, contentJoin, contentEmpty);

        }else {
            ListObjects.visibleGoneView(new View[]{contentList, recyclerView}, contentEmpty, contentPublicRoom, contentJoin, contentEmpty);

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            roomAdapter.setData(quizModelList);
            roomAdapter.setPresenter(mPresenter);
            recyclerView.setAdapter(roomAdapter);
        }
    }

    private void displayRoom(){
        if (currentRoomModel != null) {
            setHeaderRoom(currentRoomModel);
            mPresenter.displayRoomQuizList(currentRoomModel);

        }else
            mPresenter.displayCurrentRoom();
    }

    private void createQuiz(View view) {
        Fragment fragment = QuizFragment.createInstance(ListObjects.KEY_CREATE_ROOM, currentRoomModel, null);
        ListObjects.navigateTo(mContext, fragment, true).commit();
    }

    private void publishQuiz(View view){
        if (currentRoomModel != null) {
            currentRoomModel.setExpired(true);
            mPresenter.expireRoom(currentRoomModel);
        }else
            onError(new Throwable("Invalid current room."));
    }

    private void resultQuiz(RoomModel roomModel) {
        Fragment fragment = ResultFragment.createInstance(roomModel);
        ListObjects.navigateTo(mContext, fragment, true).commit();
    }

    private void takeTheRoom(View view) {
        if (currentRoomModel != null){
            ParticipantModel model = new ParticipantModel();
            model.setParticipantId(UUID.randomUUID().toString());
            model.setRoomId(currentRoomModel.getRoomId());
            model.setUserId(currentRoomModel.getUserId());
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
        Fragment fragment = QuizFragment.createInstance(ListObjects.KEY_TAKE_ROOM, roomModel, participantModel);
        ListObjects.navigateTo(mContext, fragment, true).commit();
    }

    private void signOutOverRoom(View view) {
        mPresenter.removeCurrentRoom(currentRoomModel);
        displayPublicJoin();
    }

    @Override
    public void onRoomRemoteLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomRemoteLoaded()");
        mPresenter.saveCurrentRoom(roomModel);
        setHeaderRoom(roomModel);

        if (currentUser != null)
            mPresenter.detectParticipation(currentUser, roomModel);
    }

    @Override
    public void onRoomLocalLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomLocalLoaded()");
        mContext.runOnUiThread(() -> setHeaderRoom(roomModel));

        if (currentUser != null)
            mPresenter.detectParticipation(currentUser, roomModel);
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
    public void onRoomExpired(RoomModel roomModel) {
        Log.d(TAG, "onRoomExpired");

        Snackbar.make(rootView, "Successfully publish, share this room to participants by copy the room Id.", Snackbar.LENGTH_LONG).show();
        ListObjects.fragmentManager(mContext).popBackStack();
    }

    @Override
    public void onQuestionsLoaded(List<QuizModel> quizModelList) {
        Log.d(TAG, "onRoomQuizLoaded");
        mQuestionSize = quizModelList.size();

        if (isStateByOwner)
            displayOwnerList(quizModelList);
        else
            displayPublicRoom(KEY_ROOM_CONTINUE);
    }

    @Override
    public void onQuestionsEmpty() {
        Log.d(TAG, "onQuestionsEmpty");

        displayOwnerList(Collections.emptyList());
    }

    private void copyRoomClipboard(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Room Id", currentRoomModel.getRoomId());
        if(currentRoomModel != null) {
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(mContext, "Room Id copied.", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(mContext, "Invalid room Id.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemQuizSelected(QuizModel quizModel) {
        Log.d(TAG, "onItemQuizSelected "+ quizModel.getQuizId());
    }

    @Override
    public void onParticipationExist(ParticipantModel participantModels) { currentParticipantModel = participantModels;
        Log.d(TAG, "onExistParticipation");

        Snackbar.make(rootView, "You already joined the room, please finish the quiz!", Snackbar.LENGTH_LONG).show();

        displayPublicRoom(KEY_ROOM_CONTINUE);
    }

    @Override
    public void onParticipationExpired() {
        Log.d(TAG, "onExpiredParticipate");

        Snackbar.make(rootView, "Thank's for your participation!", Snackbar.LENGTH_LONG).show();

        displayPublicRoom(KEY_ROOM_FINISH);
    }

    @Override
    public void onParticipantEmpty() {
        Log.d(TAG, "onParticipantEmpty");

        Snackbar.make(rootView, "Welcome to the room, please finish the quiz!", Snackbar.LENGTH_LONG).show();

        displayPublicRoom(KEY_ROOM_GENERAL);
    }


    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
        Snackbar.make(rootView, "Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgressShow()");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgressHide()");

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
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null){
            mPresenter.destroy();
        }
    }
}
