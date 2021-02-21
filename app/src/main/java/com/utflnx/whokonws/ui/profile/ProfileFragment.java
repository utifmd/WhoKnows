package com.utflnx.whokonws.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.ui.dashboard.DashboardFragment;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;

// TODO: 26/01/21
// 1.refresh profile by swipe

public class ProfileFragment extends Fragment implements ProfileMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity mContext;
    private ProfileMainContract.Presenter mPresenter;
    private ProfileRepository mRepository;
    private QuizRepository quizRepository;
    private View rootView;
    private UserModel currentUser = new UserModel();

    private Button btnSignOut, btnOwnerRoom;
    private TextView textProfile;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        mRepository = new ProfileRepository(context); // mCurrentUser = new User();
        quizRepository = new QuizRepository(context);
    }

    public static ProfileFragment createInstance(UserModel currentUser){
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            currentUser = (UserModel)getArguments().getSerializable(ListObjects.KEY_CURRENT_USER);

        new ProfilePresenter(this, mRepository, quizRepository);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeLayout();
        updateStateLayout(currentUser);

        return rootView;
    }

    private void initializeLayout() {
        textProfile = rootView.findViewById(R.id.singleProfileText);
        btnSignOut = rootView.findViewById(R.id.btn_sign_out);
        btnOwnerRoom = rootView.findViewById(R.id.btn_owner_room);
    }

    void updateStateLayout(UserModel currentUserModel){
        textProfile.setText(currentUserModel.getUserName());
        btnSignOut.setOnClickListener(this::signOut);
        btnOwnerRoom.setOnClickListener(this::ownerRoom);
    }

    private void ownerRoom(View view) {
        RoomOwnerFragment fragment = RoomOwnerFragment.createInstance(currentUser);

        ListObjects.navigateTo(mContext, fragment, true).commit();
    }

    private void signOut(View view) {
        mPresenter.signOut();
    }

    @Override
    public void onProfileLocalLoaded(UserModel currentUserModel) {
        Log.d(TAG, "onProfileLocalLoaded()"); //mContext.runOnUiThread(() -> updateStateLayout(currentUserModel));
    }

    @Override
    public void onProfileRemoteLoaded(UserModel currentUserModel) {
        Log.d(TAG, "onProfileRemoteLoaded()"); //updateStateLayout(currentUserModel);
    }

    @Override
    public void onProfileUpdated(UserModel currentUserModel) {
        Log.d(TAG, "onProfileUpdated()");
    }

    @Override
    public void onProfileDeleted(UserModel currentUserModel) {
        Log.d(TAG, "onProfileDeleted()");
    }

    @Override
    public void onProfileEmpty() {
        Log.d(TAG, "onProfileEmpty()");

        ListObjects.navigateTo(getContext(), new DashboardFragment(), false).commit();
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "onError()");
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgress..");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgress done.");
    }

    @Override
    public void setPresenter(ProfileMainContract.Presenter presenter) {
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
    }
}
