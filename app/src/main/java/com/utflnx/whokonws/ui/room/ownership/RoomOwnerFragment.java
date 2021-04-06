package com.utflnx.whokonws.ui.room.ownership;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.MainActivity;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomRepository;
import com.utflnx.whokonws.ui.MainPresenter;
import com.utflnx.whokonws.ui.room.ownership.extension.RoomOwnerAdapter;
import com.utflnx.whokonws.ui.room.publicity.RoomFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomOwnerFragment extends Fragment implements
        RoomOwnerMainContract.View, MainPresenter.RoomOwnerScopeListener, MainPresenter.RoomScopeListener.Callback {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity mContext;
    private RoomOwnerMainContract.Presenter mPresenter;
    private MainPresenter.RoomOwnerScopeListener.Callback mOwnerScopeCallback;
    private RoomRepository roomRepository;
    private ProfileRepository profileRepository;
    private QuizRepository quizRepository;

    private View rootView, contentSubmit, contentList, contentEmpty, btnClosePost, btnCreateRoom;
    private RecyclerView recyclerView;
    private RoomOwnerAdapter adapter;
    private TextInputLayout inpTitle, inpDesc, inpMinute;

    private UserModel currentUser;
    private final Boolean isAllFabVisible = false;

    private ArrayList<RoomModel> mRoomModelArrayList = new ArrayList<>();

    public static RoomOwnerFragment createInstance(UserModel currentUser){
        RoomOwnerFragment fragment = new RoomOwnerFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    public RoomOwnerFragment(){
        super(R.layout.fragment_room_owner);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        roomRepository = new RoomRepository(context);
        profileRepository = new ProfileRepository(context);
        quizRepository = new QuizRepository(context);
        adapter = new RoomOwnerAdapter(context); //        if (context instanceof MainPresenter.RoomOwnerScopeListener.Callback) mOwnerScopeCallback = (MainPresenter.RoomOwnerScopeListener.Callback) context; //        else throw new ClassCastException(context.toString()+" must have implement callback");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = (UserModel) requireArguments().getSerializable(ListObjects.KEY_CURRENT_USER);

        ((MainActivity)mContext).setRoomOwnerScopeListener(this);
        ListObjects.handleOnBackPressed(mContext, this);

        new RoomOwnerPresenter(this, roomRepository, profileRepository, quizRepository);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (currentUser != null) mContext.runOnUiThread(() -> mPresenter.displayOwnerRoom(currentUser));

        rootView = view;
        initializeLayout(view);
    }

    @Override
    public void onRoomOwnerLoaded(List<RoomModel> roomModels) {
        Log.d(TAG, "onRoomOwnerLoaded(Room room)");
        mRoomModelArrayList = (ArrayList<RoomModel>) roomModels;

        mContext.runOnUiThread(() -> replaceWithListRoom(rootView));
    }

    @Override
    public void onRoomOwnerEmpty() {
        Log.d(TAG, "onRoomEmpty()");

        mContext.runOnUiThread(() -> replaceWithEmpty(rootView));
    }

    @Override
    public void onRoomOwnerSaved(RoomModel roomModel) {
        Log.d(TAG, "onRoomSaved(Room room)");
        mRoomModelArrayList.add(roomModel);

        mContext.runOnUiThread(() -> replaceWithListRoom(rootView));
    }

    @Override
    public void onRoomItemSelected(RoomModel roomModel) {
        Log.d(TAG, "onRoomItemSelected: "+ roomModel.getTitle());

        mOwnerScopeCallback.onInvalidateToolbar(MainActivity.STATE_DEFAULT);

        RoomFragment roomFragment = RoomFragment.createInstance(currentUser, roomModel, true);
        roomFragment.setRoomCallback(this);

        ListObjects.navigateTo(mContext, roomFragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onRoomItemDeleteSelected(RoomModel roomModel, int position) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext);
        if(!roomModel.isExpired()) {
            alertDialogBuilder.setTitle("Are you sure to remove the " + roomModel.getTitle().toLowerCase() + "?").setNegativeButton("remove", (dialogInterface, i) -> {
                mRoomModelArrayList.remove(roomModel);
                mPresenter.removeRoom(roomModel);
            }).show();
            return true;
        }
        return false;
    }

    @Override
    public void onRoomDeleted(RoomModel roomModel) {
        Log.d(TAG, "onRoomDeleted");

        mContext.runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            if (mRoomModelArrayList.isEmpty())
                replaceWithEmpty(rootView);

            mOwnerScopeCallback.onNotify(roomModel.getTitle()+" successfully removed.", Snackbar.LENGTH_SHORT);
        });
    }

    @Override
    public void onRoomLoaded(RoomModel roomModel) {
        Log.d(TAG, "onRoomLoaded(Room room)");
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
        mOwnerScopeCallback.onNotify("Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgress...");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgress done.");
    }

    @Override
    public void onItemJoinClickListener() {
        RoomFragment roomFragment = RoomFragment.createInstance(currentUser, null, false);
        roomFragment.setRoomCallback(this);
        ListObjects.navigateTo(mContext, roomFragment).addToBackStack("ownRoom").commit();
    }

    @Override
    public void onItemSubmitClickListener() {
        mContext.runOnUiThread(() -> replaceWithSubmitRoom(rootView));
    }

    private void replaceWithSubmitRoom(View btnView) {
        ListObjects.visibleGoneView(new View[]{contentSubmit}, contentList);
    }

    private void replaceWithListRoom(View btnView) {
        mOwnerScopeCallback.onInvalidateToolbar(MainActivity.STATE_OWNER_ROOM);

        ListObjects.visibleGoneView(new View[]{contentList, recyclerView}, contentEmpty, contentSubmit);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setHasFixedSize(true);
        adapter.setPresenter(mPresenter);
        adapter.setData(mRoomModelArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void replaceWithEmpty(View btnView) {
        ListObjects.visibleGoneView(new View[]{contentEmpty}, recyclerView);
    }

    private void createRoom(View btnView) {
        RoomModel roomModel = new RoomModel();
        EditText etTitle = inpTitle.getEditText(), etDesc = inpDesc.getEditText(), etMinute = inpMinute.getEditText();

        if (etTitle != null && etDesc != null && etMinute != null){
            if (!etTitle.getText().toString().isEmpty() && !etDesc.getText().toString().isEmpty() && !etMinute.getText().toString().isEmpty()){

                roomModel.setRoomId(UUID.randomUUID().toString());
                roomModel.setUserId(currentUser.getUserId());
                roomModel.setTitle(etTitle.getText().toString().trim());
                roomModel.setDesc(etDesc.getText().toString().trim());
                roomModel.setMinute(etMinute.getText().toString().trim());
                roomModel.setExpire(false);

                mPresenter.createRoom(roomModel);
            }else
                mOwnerScopeCallback.onNotify(getString(R.string.form_cant_empty), Snackbar.LENGTH_SHORT);
        }else
            mOwnerScopeCallback.onNotify(getString(R.string.invalid_input), Snackbar.LENGTH_SHORT);
    }

    private void initializeLayout(View rootView) {
        contentSubmit = rootView.findViewById(R.id.contentSubmit);
        contentList = rootView.findViewById(R.id.contentList);
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        contentEmpty = rootView.findViewById(R.id.contentEmpty);
        btnCreateRoom = rootView.findViewById(R.id.btn_create_room);
        btnClosePost = rootView.findViewById(R.id.btn_close_submit);
        inpTitle = rootView.findViewById(R.id.text_input_room_title);
        inpDesc = rootView.findViewById(R.id.text_input_room_desc);
        inpMinute = rootView.findViewById(R.id.text_input_room_time);

        btnCreateRoom.setOnClickListener(this::createRoom);
        btnClosePost.setOnClickListener(this::replaceWithListRoom);
    }

    public void setOwnerCallback(MainPresenter.RoomOwnerScopeListener.Callback ownerScopeCallback){
        mOwnerScopeCallback = ownerScopeCallback;
    }

    @Override
    public void setPresenter(RoomOwnerMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.destroy();
        }
        if(rootView != null){
            rootView = null;
        }
        if (mOwnerScopeCallback != null){
            mOwnerScopeCallback = null;
        }
    }

    @Override
    public void onDisplaySingleTab(int stateOfTab, UserModel currentUser) {
        mOwnerScopeCallback.onDisplaySingleTab(stateOfTab, currentUser);
    }

    @Override
    public void onInvalidateToolbar(int stated) {
        mOwnerScopeCallback.onInvalidateToolbar(stated);
    }

    @Override
    public void onInactivateToolbar() {
        mOwnerScopeCallback.onInactivateToolbar();
    }

    @Override
    public void onNotify(String message, int duration) {
        mOwnerScopeCallback.onNotify(message, duration);
    }
}
