package com.utflnx.whokonws.ui;

import com.utflnx.whokonws.model.UserModel;

public interface MainPresenter {
    interface AuthenticationListener {
        void onSignedIn(UserModel currentUser);
        void onSignedOut();
    }

    interface DashScopeListener {
        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }

    interface ExploreScopeListener {
        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }

    interface ProfileScopeListener {
        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }

    interface ResultScopeListener {
        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }

    interface RoomOwnerScopeListener {
        void onItemJoinClickListener();
        void onItemSubmitClickListener();

        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration); //            void onProgressShow(long loadingSize); //            void onProgressHide();
        }
    }

    interface RoomScopeListener {
        // void onItemJoinClickListener(); // void onItemSubmitClickListener();

        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }

    interface QuizScopeListener {
        //void finishParticipantTakenQuiz(); // void onItemSubmitClickListener();

        interface Callback{
            void onDisplaySingleTab(int stateOfTab, UserModel currentUser);
            void onInvalidateToolbar(int stated);
            void onInactivateToolbar();
            void onNotify(String message, int duration);
        }
    }
}
