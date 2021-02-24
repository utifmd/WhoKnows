package com.utflnx.whokonws.ui.room.ownership;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.repo.room.RoomRepository;
import com.utflnx.whokonws.ui.room.ownership.extension.RoomOwnerAdapter;
import com.utflnx.whokonws.ui.room.publicity.RoomFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomOwnerFragment extends Fragment implements RoomOwnerMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity mContext;
    private RoomOwnerMainContract.Presenter mPresenter;
    private RoomRepository roomRepository;
    private ProfileRepository profileRepository;
    private QuizRepository quizRepository;

    private View rootView, contentSubmit, contentList, contentEmpty, btnClosePost, btnCreateRoom;
    private RecyclerView recyclerView;
    private RoomOwnerAdapter adapter;
    private TextInputLayout inpTitle, inpDesc, inpMinute;

    private UserModel currentUser;
    private Boolean isAllFabVisible = false;

    private ArrayList<RoomModel> mRoomModelArrayList = new ArrayList<>();

    @Override
    public void setPresenter(RoomOwnerMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        roomRepository = new RoomRepository(context);
        profileRepository = new ProfileRepository(context);
        quizRepository = new QuizRepository(context);
        adapter = new RoomOwnerAdapter(context);
    }

    public static RoomOwnerFragment createInstance(UserModel currentUser){
        RoomOwnerFragment fragment = new RoomOwnerFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentUser = (UserModel) getArguments().getSerializable(ListObjects.KEY_CURRENT_USER);
        }

        Log.d(TAG, "onCreate user: "+currentUser.getEmail());

        new RoomOwnerPresenter(this, roomRepository, profileRepository, quizRepository);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_room_owner, container, false);

        initializeLayout(rootView);

        if (currentUser != null) mPresenter.displayOwnerRoom(currentUser);

        return rootView;
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

        Fragment roomFragment = RoomFragment.createInstance(currentUser, roomModel, true);
        ListObjects.navigateTo(mContext, roomFragment, true).commit();
    }

    @Override
    public boolean onRoomItemLongSelected(RoomModel roomModel, int position) {
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

            Snackbar.make(rootView, roomModel.getTitle()+" successfully removed.", Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRoomLoaded(RoomModel roomModel) {
        Log.d(TAG, "onRoomLoaded(Room room)");
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == R.id.page_submit_room)
            mContext.runOnUiThread(() -> replaceWithSubmitRoom(rootView));

        return true; //super.onOptionsItemSelected(item);
    }

    private void replaceWithSubmitRoom(View btnView) {
        ListObjects.visibleGoneView(new View[]{contentSubmit}, contentList);
    }

    private void replaceWithListRoom(View btnView) {
        ListObjects.visibleGoneView(new View[]{contentList, recyclerView}, contentEmpty, contentSubmit);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
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
                Snackbar.make(rootView, R.string.form_cant_empty, Snackbar.LENGTH_SHORT).show();
        }else
            Snackbar.make(rootView, R.string.invalid_input, Snackbar.LENGTH_SHORT).show();
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
        ((Toolbar)rootView.findViewById(R.id.toolbar_fragment)).setOnMenuItemClickListener(this::onOptionsItemSelected);
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
        if (mPresenter != null){
            mPresenter.destroy();
        }
    }
}
