package com.utflnx.whokonws.ui;

import com.utflnx.whokonws.model.UserModel;

public interface AuthenticationListener {
    void onSignedIn(UserModel currentUser);
    void onSignedOut();
}