package com.utflnx.whokonws.ui.room.publicity;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.ParticipantModel;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.repo.participant.ParticipateRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomRepository;
import com.utflnx.whokonws.ui.quiz.QuizFragment;
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
    private QuizRepository quizRepository;
    private ParticipateRepository participateRepository;
    private FragmentActivity mContext;
    private View rootView, contentPublicRoom, contentJoin, contentList, btnCloseContent;
    private TextView roomTitle, roomDesc, roomTime;
    private TextInputLayout inpRoomId;
    private Button btnJoinRoom, btnAddRoomQuiz, btnTakeRoom;

    private RoomModel currentRoomModel = null;
    private int mQuestionSize = 0;
    private boolean isStateByOwner = false;

    private RoomAdapter roomAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        roomRepository = new RoomRepository(context);
        quizRepository = new QuizRepository(context);
        participateRepository = new ParticipateRepository(context);
        roomAdapter = new RoomAdapter(context);
    }

    public static RoomFragment createInstance(RoomModel roomModel, boolean isByOwner){
        RoomFragment roomFragment = new RoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FRAGMENT, roomModel);
        bundle.putBoolean(KEY_IS_STATE_BY_OWNER, isByOwner);

        roomFragment.setArguments(bundle);
        return roomFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RoomPresenter(this, roomRepository, quizRepository, participateRepository);

        if (getArguments() != null) {
            currentRoomModel = (RoomModel) getArguments().getSerializable(KEY_FRAGMENT);
            isStateByOwner = getArguments().getBoolean(KEY_IS_STATE_BY_OWNER);
        }
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
            if (!etRoomId.getText().toString().isEmpty())
                mPresenter.joinRoom(etRoomId.getText().toString().trim());
            else
                Snackbar.make(rootView, R.string.form_cant_empty, Snackbar.LENGTH_SHORT).show();
        }else
            Snackbar.make(rootView, R.string.invalid_input, Snackbar.LENGTH_SHORT).show();
    }

    private void initializeLayout(View rootView){
        contentPublicRoom = rootView.findViewById(R.id.contentPublicRoom);
        contentJoin = rootView.findViewById(R.id.contentJoin);
        contentList = rootView.findViewById(R.id.contentList);

        roomTitle = rootView.findViewById(R.id.item_title);
        roomDesc = rootView.findViewById(R.id.room_desc);
        roomTime = rootView.findViewById(R.id.room_time);

        btnCloseContent = rootView.findViewById(R.id.btn_close_content);
        btnJoinRoom = rootView.findViewById(R.id.btn_join_room);
        btnAddRoomQuiz = rootView.findViewById(R.id.btn_add_room_quiz);
        btnTakeRoom = rootView.findViewById(R.id.btn_take_room);
        inpRoomId = rootView.findViewById(R.id.text_input_room_id);

        recyclerView = rootView.findViewById(R.id.mRecyclerView);
    }

    private void setHeaderRoom(RoomModel roomModel) {
        roomTitle.setText(roomModel.getTitle());
        roomDesc.setText(roomModel.getDesc());
        roomTime.setText(roomModel.getMinute());
    }

    private void displayPublicJoin(){
        ListObjects.visibleGoneView(new View[]{contentJoin}, contentPublicRoom, contentList);

        btnJoinRoom.setOnClickListener(this::joinRoom);
    }

    private void displayPublicRoom(boolean isDone){
        ListObjects.visibleGoneView(new View[]{contentPublicRoom}, contentJoin, contentList);

        if (isDone) {
            btnTakeRoom.setEnabled(false); //btnTakeRoom.setOnClickListener(this::navigateToQuiz);
            btnTakeRoom.setText(R.string.finish);
        } else {
            btnTakeRoom.setEnabled(true);
            btnTakeRoom.setOnClickListener(this::takeTheRoom);
        }

        btnCloseContent.setOnClickListener(this::signOutOverRoom);

    }

    private void displayOwnerList(List<QuizModel> quizModelList){
        ListObjects.visibleGoneView(new View[]{contentList}, contentPublicRoom, contentJoin);

        btnAddRoomQuiz.setOnClickListener(this::createQuiz);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        roomAdapter.setData(quizModelList);
        roomAdapter.setPresenter(mPresenter);
        recyclerView.setAdapter(roomAdapter);
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
    public void onRoomLocalLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomLocalLoaded()");

        //Snackbar.make(rootView, "You already join the "+ roomModel.getTitle()+" room, please finish the quiz!", Snackbar.LENGTH_LONG).show();

        displayParticipation(roomModel, true);
    }

    @Override
    public void onRoomRemoteLoaded(RoomModel roomModel) { currentRoomModel = roomModel;
        Log.d(TAG, "onRoomRemoteLoaded()");
        mPresenter.saveCurrentRoom(roomModel);

        Snackbar.make(rootView, "Successfully join the "+ roomModel.getTitle()+" room.", Snackbar.LENGTH_LONG).show();

        displayParticipation(roomModel, false);
    }

    private void displayParticipation(RoomModel roomModel, boolean isLocal){
        mContext.runOnUiThread(() -> setHeaderRoom(roomModel));

       if (isLocal) mPresenter.displayCurrentParticipate(roomModel);
       else displayPublicRoom(false);
    }

    @Override
    public void onParticipantRemoteLoaded(ParticipantModel participantModel) {
        Log.d(TAG, "onParticipantRemoteLoaded");

        Snackbar.make(rootView, "You already participate for the room, please finish the quiz!", Snackbar.LENGTH_LONG).show();

        displayPublicRoom(false);
    }

    @Override
    public void onParticipantEmpty() {
        Log.d(TAG, "onParticipantEmpty");  //6fcb5d7e-a85b-40c5-894c-874977982c54

        displayPublicRoom(false); // if (currentRoomModel != null) mPresenter.displayCurrentRoom();
    }

    @Override
    public void onExpiredParticipate() {
        Log.d(TAG, "onExpiredParticipate");

        Snackbar.make(rootView, "Thank you for participation!", Snackbar.LENGTH_LONG).show();

        displayPublicRoom(true);
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
    public void onRoomTakenSaved(ParticipantModel participantModel) { // currentParticipantModel = participantModel;
        Log.d(TAG, "onRoomTakenSaved()"); //mPresenter.saveCurrentParticipant(participantModel);

        if(currentRoomModel != null) navigateToQuiz(currentRoomModel, participantModel);
    }

    @Override
    public void onQuestionsLoaded(List<QuizModel> quizModelList) {
        Log.d(TAG, "onRoomQuizLoaded");
        mQuestionSize = quizModelList.size();

        if (isStateByOwner)
            displayOwnerList(quizModelList);
        else
            displayPublicRoom(true);
    }


    @Override
    public void onQuestionsEmpty() {
        Log.d(TAG, "onQuestionsEmpty");

        displayOwnerList(Collections.emptyList());
        Snackbar.make(rootView, R.string.complete_room_pls, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemQuizSelected(QuizModel quizModel) {
        Log.d(TAG, "onItemQuizSelected "+ quizModel.getQuizId());
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
