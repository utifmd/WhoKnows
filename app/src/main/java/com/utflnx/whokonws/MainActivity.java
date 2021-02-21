package com.utflnx.whokonws;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileDataContract;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.ui.dashboard.DashboardFragment;
import com.utflnx.whokonws.ui.profile.ProfileFragment;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerFragment;


// TODO: 03/02/21
/* 1.
 * 2. Update room to expired ~> give number each questions
 * 3. Display room result
 * 4. Display owner room indicators
 * 5. Beautify UI
 *
 * */

public class MainActivity extends AppCompatActivity{
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        ListObjects.navigateTo(this, new DashboardFragment(), false).commit();
    }
}