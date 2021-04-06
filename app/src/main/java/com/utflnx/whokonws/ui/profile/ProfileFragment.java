package com.utflnx.whokonws.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.repo.quiz.QuizRepository;
import com.utflnx.whokonws.ui.MainPresenter;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;

public class ProfileFragment extends Fragment implements ProfileMainContract.View {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity mContext;
    private ProfileMainContract.Presenter mPresenter;
    private MainPresenter.ProfileScopeListener.Callback profileCallback;
    private ProfileRepository mRepository;
    private QuizRepository quizRepository;
    private View rootView;
    private UserModel currentUser = new UserModel();

    private Button btnSignOut;
    private TextView tvFullName, tvPhone, tvMail;

    private MainPresenter.AuthenticationListener mAuthenticationListener;

    public static ProfileFragment createInstance(UserModel currentUser){
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ListObjects.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = (FragmentActivity) context;
        mRepository = new ProfileRepository(context); // mCurrentUser = new User();
        quizRepository = new QuizRepository(context);

        ListObjects.handleOnBackPressed(mContext, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ProfilePresenter(this, mRepository, quizRepository);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currentUser = (UserModel) requireArguments().getSerializable(ListObjects.KEY_CURRENT_USER);
        rootView = view;

        initializeLayout();
        updateStateLayout(currentUser);
    }

    private void initializeLayout() {
        tvFullName = rootView.findViewById(R.id.tv_full_name);
        tvPhone = rootView.findViewById(R.id.tv_phone);
        tvMail = rootView.findViewById(R.id.tv_mail);
        btnSignOut = rootView.findViewById(R.id.btn_sign_out);
    }

    void updateStateLayout(UserModel currentUserModel){
        tvFullName.setText(currentUserModel.getFullName());
        tvPhone.setText(currentUserModel.getPhone());
        tvMail.setText(currentUserModel.getEmail());

        btnSignOut.setOnClickListener(this::signOut);
    }

    private void ownerRoom(View view) {
        RoomOwnerFragment fragment = RoomOwnerFragment.createInstance(currentUser);

        ListObjects.navigateTo(mContext, fragment).commit();
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

        mAuthenticationListener.onSignedOut(); //ListObjects.navigateTo(getContext(), new DashboardFragment(), false).commit();
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
        profileCallback.onNotify("Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgress..");
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgress done.");
    }

    public void setProfileCallback(MainPresenter.ProfileScopeListener.Callback callback) {
        profileCallback = callback;
    }

    @Override
    public void setPresenter(ProfileMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setAuthenticationListener(MainPresenter.AuthenticationListener authListener){
        this.mAuthenticationListener = authListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() { Log.d(TAG, "onDestroy");
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.destroy();
        }
        if(rootView != null){
            rootView = null;
        }
        if (profileCallback != null){
            profileCallback = null;
        }
    }
}
