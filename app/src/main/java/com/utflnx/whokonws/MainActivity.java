package com.utflnx.whokonws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.ui.MainPresenter;
import com.utflnx.whokonws.ui.dashboard.DashboardFragment;
import com.utflnx.whokonws.ui.explore.ExploreFragment;
import com.utflnx.whokonws.ui.profile.ProfileFragment;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;


// TODO: 24/02/21
/*
 * - [x] empty content to explore
 * - [x] onError() snack bar trigger mainActivity view
 * - [x] one question on quiz room had an issue after submit & expire result
 * 1. default answer had an issue still null
 * 2. explore item "participate button should implement"
 * 3. one more relation it is Result Table -> roomId to get participant size
 * 4. warning: There are multiple good constructors and Room will pick the no-arg constructor. You can use the @Ignore annotation to eliminate unwanted constructors.
 * */

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ProfileDataContract.LoadedProfileCallBack,
        MainPresenter.AuthenticationListener,
        MainPresenter.DashScopeListener.Callback,
        MainPresenter.ProfileScopeListener.Callback,
        MainPresenter.ExploreScopeListener.Callback,
        MainPresenter.RoomOwnerScopeListener.Callback,
        MainPresenter.RoomScopeListener.Callback {

    private final String TAG = getClass().getSimpleName();
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomBar;
    private UserModel mCurrentUser = null;

    public static final int STATE_PROFILE = 101;
    public static final int STATE_OWNER_ROOM = 102;
    public static final int STATE_DEFAULT = 100;
    public static final int STATE_DETAILED = 103;
    private int STATE;

    private MainPresenter.RoomOwnerScopeListener mOwnerScopeListener;

    public MainActivity(){
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //setContentView(R.layout.activity_main); Log.d(TAG, "onCreate()");

        appBarLayout = findViewById(R.id.app_bar);
        bottomBar = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar_main);

        initializeFragmentSetter();
    }

    @Override
    public void onProfileLoaded(UserModel currentUserModel) {
        Log.d(TAG, "onProfileLoaded");
        mCurrentUser = currentUserModel;

        runOnUiThread(() -> displayProfile(currentUserModel));
    }

    @Override
    public void onProfileEmpty() {
        Log.d(TAG, "onProfileEmpty");

        runOnUiThread(this::displayDashboard);
    }

    @Override
    public void onError(Throwable t) { bottomBar.setVisibility(View.GONE);
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
    }

    @Override
    public void onSignedIn(UserModel currentUser) {
        Log.d(TAG, "onSignIn");
        mCurrentUser = currentUser;
        bottomBar.setVisibility(View.VISIBLE);

        runOnUiThread(() -> displayProfile(currentUser));
    }

    @Override
    public void onSignedOut() {
        Log.d(TAG, "onSignedOut");

        try {
            mCurrentUser = null;
            runOnUiThread(this::displayDashboard);

        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        switch (STATE){
            case STATE_OWNER_ROOM:
                inflater.inflate(R.menu.menu_owner_room, menu);
                break;
            case STATE_PROFILE:
                inflater.inflate(R.menu.menu_profile, menu);
                break;
            case STATE_DETAILED:
                inflater.inflate(R.menu.menu_close, menu);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.page_join_room:
                STATE = STATE_DEFAULT; invalidateOptionsMenu();
                mOwnerScopeListener.onItemJoinClickListener();
                break;
            case R.id.page_submit_room:
                STATE = STATE_DEFAULT; invalidateOptionsMenu();
                mOwnerScopeListener.onItemSubmitClickListener();
                break;
            case R.id.page_about:
                Log.d(TAG, "page about selected.");
                break;
            default:
                return false;
        }
        return true;
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.page_profile:
                if (mCurrentUser != null) runOnUiThread(() -> displayProfile(mCurrentUser));
                break;
            case R.id.page_question:
                if (mCurrentUser != null) runOnUiThread(() -> displayOwnerRoom(mCurrentUser));
                break;
            case R.id.page_explore:
                if (mCurrentUser != null) runOnUiThread(() -> displayExplore(mCurrentUser));
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onDisplaySingleTab(int stateOfTab, UserModel currentUser) {
        switch (stateOfTab){
            case STATE_OWNER_ROOM: displayOwnerRoom(currentUser); break;
            case STATE_PROFILE: displayProfile(currentUser); break;
            case STATE_DEFAULT: displayExplore(currentUser); break;
            default: displayDashboard(); break;
        }
    }

    private void displayProfile(UserModel currentUserModel) {
        STATE = STATE_PROFILE; invalidateOptionsMenu();
        ProfileFragment mProfile = ProfileFragment.createInstance(currentUserModel);
        mProfile.setAuthenticationListener(this);
        mProfile.setProfileCallback(this);

        ListObjects.navigateTo(this, mProfile).commit();
        navigationShow();
    }

    private void displayOwnerRoom(UserModel currentUserModel) {
        STATE = STATE_OWNER_ROOM; invalidateOptionsMenu();
        RoomOwnerFragment mRoomOwner = RoomOwnerFragment.createInstance(currentUserModel);
        mRoomOwner.setOwnerCallback(this);

        ListObjects.navigateTo(this, mRoomOwner).commit();
        navigationShow();
    }

    private void displayExplore(UserModel mCurrentUser) {
        STATE = STATE_DEFAULT; invalidateOptionsMenu();
        ExploreFragment mExplore = ExploreFragment.createInstance(mCurrentUser);
        mExplore.setExploreCallback(this);

        ListObjects.navigateTo(this, mExplore).commit();
        navigationShow();
    }

    protected void displayDashboard() {
        DashboardFragment mDashboard = new DashboardFragment();
        mDashboard.setAuthListener(this);
        mDashboard.setDashCallback(this);

        ListObjects.navigateTo(this, mDashboard).commit();
        navigationHide();
    }

    private void navigationShow(){
        appBarLayout.setExpanded(false);
        bottomBar.setVisibility(View.VISIBLE);
    }

    private void navigationHide(){
        bottomBar.setVisibility(View.GONE); // appBarLayout = (AppBarLayout)findViewById(R.id.appBar); appBarLayout.setExpanded(false, true);
    }

    @Override
    public void onInvalidateToolbar(int stated) {
        STATE = stated;
        invalidateOptionsMenu();
    }

    @Override
    public void onInactivateToolbar() {
        appBarLayout.setExpanded(false);
        appBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void onNotify(String message, int duration) {
        Log.d(TAG, "onNotify: "+message);
        Snackbar.make(findViewById(android.R.id.content), message, duration).show();
    }

    private void initializeFragmentSetter() {
        ProfileRepository profileRepository = new ProfileRepository(this);

        profileRepository.getLocalProfile(this);
        setSupportActionBar(toolbar);
        bottomBar.setOnNavigationItemSelectedListener(this);
    }

    public void setRoomOwnerScopeListener(MainPresenter.RoomOwnerScopeListener ownerRoomListener){
        mOwnerScopeListener = ownerRoomListener;
    } //    public void setRoomScopeListener(MainPresenter.RoomScopeListener roomListener){ //        mRoomScopeListener = roomListener; //    }
}
