package com.utflnx.whokonws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.ui.AuthenticationListener;
import com.utflnx.whokonws.ui.dashboard.DashboardFragment;
import com.utflnx.whokonws.ui.explore.ExploreFragment;
import com.utflnx.whokonws.ui.profile.ProfileFragment;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;


// TODO: 23/02/21  
/* 1. explore features
 * 2.
 * */

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, 
        ProfileDataContract.LoadedProfileCallBack,
        AuthenticationListener {
    
    private final String TAG = getClass().getSimpleName();
    private BottomNavigationView bottomBar;
    private UserModel mCurrentUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); Log.d(TAG, "onCreate()");
        bottomBar = findViewById(R.id.bottom_navigation);

        ProfileRepository profileRepository = new ProfileRepository(this);
        profileRepository.getLocalProfile(this);

        bottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onProfileLoaded(UserModel currentUserModel) { mCurrentUser = currentUserModel;
        Log.d(TAG, "onProfileLoaded");

        displayProfile(currentUserModel);
    }

    @Override
    public void onProfileEmpty() {
        Log.d(TAG, "onProfileEmpty");

        displayDashboard();
    }

    @Override
    public void onError(Throwable t) { bottomBar.setVisibility(View.GONE);
        Log.d(TAG, "Sorry, "+t.getLocalizedMessage());
    }

    @Override
    public void onSignedIn(UserModel currentUser) { mCurrentUser = currentUser;
        Log.d(TAG, "onSignIn"); bottomBar.setVisibility(View.VISIBLE);

        displayProfile(currentUser);
    }

    @Override
    public void onSignedOut() {
        Log.d(TAG, "onSignedOut");

        displayDashboard();
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.page_profile:
                if (mCurrentUser != null) displayProfile(mCurrentUser);
                break;
            case R.id.page_question:
                if (mCurrentUser != null) displayOwnerRoom(mCurrentUser);
                break;
            case R.id.page_explore:
                if (mCurrentUser != null) displayExplore(mCurrentUser);
                break;
        }
        return true;
    }

    private void displayProfile(UserModel currentUserModel) {
        ProfileFragment profile = ProfileFragment.createInstance(currentUserModel);
        profile.setAuthenticationListener(this);

        ListObjects.navigateTo(this, profile, false).commit(); bottomBar.setVisibility(View.VISIBLE);
    }

    private void displayOwnerRoom(UserModel currentUserModel) {
        RoomOwnerFragment room = RoomOwnerFragment.createInstance(currentUserModel);

        ListObjects.navigateTo(this, room, false).commit(); bottomBar.setVisibility(View.VISIBLE);
    }

    private void displayExplore(UserModel mCurrentUser) {
        ExploreFragment explore = ExploreFragment.createInstance(mCurrentUser);

        ListObjects.navigateTo(this, explore, false).commit(); bottomBar.setVisibility(View.VISIBLE);
    }

    private void displayDashboard() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setAuthListener(this);

        ListObjects.navigateTo(this, dashboardFragment, false).commit(); bottomBar.setVisibility(View.GONE);
    }
}