package com.utflnx.whokonws.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.api.utils.ListObjects;
import com.utflnx.whokonws.model.UserModel;
import com.utflnx.whokonws.repo.profile.ProfileRepository;
import com.utflnx.whokonws.ui.explore.ExploreFragment;
import com.utflnx.whokonws.ui.profile.ProfileFragment;

import java.util.UUID;

public class DashboardFragment extends Fragment implements DashboardContract.View {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity mContext;
    private DashboardContract.Presenter mPresenter;
    private ProfileRepository profileRepository;

    private View rootView, contentSignIn, contentRegister, btnCloseContent, btnFooter;
    private Button btnSubmit, btnExplore;
    private TextInputLayout textInputEmailIn, textInputPass, textInputFullName, textInputPhone, textInputEmailReg, textInputNewPassword, textInputConfirmPassword;

    private final int VIEW_TYPE_GENERAL = 0;
    private final int VIEW_TYPE_SIGN_IN = 1;
    private final int VIEW_TYPE_REGISTER = 2;

    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
        profileRepository = new ProfileRepository(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DashboardPresenter(this, profileRepository);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeLayout(rootView);
        updateStateLayout(VIEW_TYPE_GENERAL);

        return rootView;
    }

    @Override
    public void onProfileLoaded(UserModel currentUserModel) {
        Log.d(TAG, "onProfileLoaded()");
        ListObjects.navigateTo(getContext(), new ProfileFragment(), false).commit();
    }

    @Override
    public void onSignedIn(UserModel currentUserModel) {
        Log.d(TAG, "onSignedIn()");
        mPresenter.saveCurrentUser(currentUserModel);
        Snackbar.make(rootView, "Successfully sign in as "+ currentUserModel.getFullName(), Snackbar.LENGTH_LONG).show();

        ListObjects.navigateTo(getContext(), new ProfileFragment(), false).commit();
    }

    @Override
    public void onProfileEmpty() {
        Log.d(TAG, "onGuestMode()");
    }

    @Override
    public void onProfileSaved(UserModel userModel) {
        Log.d(TAG, "onProfileSaved()");

        if (textInputEmailIn.getEditText() != null && textInputPass.getEditText() != null){
            this.displaySignInForm(rootView);

            textInputEmailIn.getEditText().setText(userModel.getEmail());
            textInputPass.getEditText().setText(userModel.getPassword());
        }

        Snackbar.make(rootView, "Successfully signed up as "+ userModel.getEmail()+", please login.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileDeleted(UserModel userModel) {}

    @Override
    public void onSignedOut() {
        Log.d(TAG, "onSignedOut()");
        ListObjects.navigateTo(getContext(), new DashboardFragment(), false).commit();
    }

    @Override
    public void onProgressShow() {
        Log.d(TAG, "onProgress...");
        mContext.runOnUiThread(() -> btnSubmit.setEnabled(false));
    }

    @Override
    public void onProgressHide() {
        Log.d(TAG, "onProgress done.");
        mContext.runOnUiThread(() -> btnSubmit.setEnabled(true));
    }

    @Override
    public void onError(Throwable t) {
        btnSubmit.setEnabled(true);
        Log.d(TAG, "Error "+t.getLocalizedMessage());
        Snackbar.make(rootView, "Sorry, "+t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    void updateStateLayout(int STATE){
        switch (STATE) {
            case VIEW_TYPE_SIGN_IN:
                btnSubmit.setOnClickListener(this::signIn);
                break;
            case VIEW_TYPE_REGISTER:
                btnSubmit.setOnClickListener(this::signUp);
                break;
            default:
                btnSubmit.setOnClickListener(this::displaySignInForm);
                break;
        }

        btnFooter.setOnClickListener(this::displayRegisterForm);
        btnCloseContent.setOnClickListener(this::displayGeneral);
        btnExplore.setOnClickListener(viewSubmit -> ListObjects.navigateTo(getContext(), new ExploreFragment(), true).commit());
    }

    private void signIn(View view) {
        EditText etEmailOrUsername = textInputEmailIn.getEditText(), etPass = textInputPass.getEditText();
        UserModel userModel = new UserModel();

        if (etEmailOrUsername != null && etPass != null){
            if (!etEmailOrUsername.getText().toString().isEmpty() && !etPass.getText().toString().isEmpty()){

                userModel.setEmail(etEmailOrUsername.getText().toString().trim());
                userModel.setPassword(etPass.getText().toString().trim());

                mPresenter.signIn(userModel);
            }else
                Snackbar.make(rootView, "Email or password can't be empty!", Snackbar.LENGTH_LONG).show();
        }else
            Snackbar.make(rootView, "Invalid input system error.", Snackbar.LENGTH_LONG).show();
    }

    private void signUp(View view) {
        EditText etEmail = textInputEmailReg.getEditText(),
                etName = textInputFullName.getEditText(),
                etPhone = textInputPhone.getEditText(),
                etConfirmPass = textInputConfirmPassword.getEditText(),
                etNewPass = textInputNewPassword.getEditText();

        UserModel userModel = new UserModel();
        if (etEmail != null && etName != null && etPhone != null && etConfirmPass != null && etNewPass != null){
            if (!etName.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && !etPhone.getText().toString().isEmpty() && !etConfirmPass.getText().toString().isEmpty() && !etNewPass.getText().toString().isEmpty()){
                if (etNewPass.getText().toString().trim().equals(etConfirmPass.getText().toString().trim())){

                    userModel.setUserId(UUID.randomUUID().toString());
                    userModel.setFullName(etName.getText().toString().trim());
                    userModel.setUserName(etEmail.getText().toString().trim().split("@")[0]);
                    userModel.setPhone(etPhone.getText().toString().trim());
                    userModel.setEmail(etEmail.getText().toString().trim());
                    userModel.setPassword(etConfirmPass.getText().toString().trim());

                    mPresenter.signUp(userModel);
                }else
                    Snackbar.make(rootView, "Sorry password does'nt match!", Snackbar.LENGTH_LONG).show();
            }else
                Snackbar.make(rootView, R.string.form_cant_empty , Snackbar.LENGTH_LONG).show();
        }else
            Snackbar.make(rootView, R.string.invalid_input, Snackbar.LENGTH_LONG).show();
    }

    private void displayGeneral(View view) {
        updateStateLayout(VIEW_TYPE_GENERAL);

        btnSubmit.setText(R.string.sign_in);
        ListObjects.visibleGoneView(new View[]{btnExplore, btnFooter}, btnCloseContent, contentRegister, contentSignIn);
    }

    private void displaySignInForm(View view) {
        updateStateLayout(VIEW_TYPE_SIGN_IN);

        btnSubmit.setText(R.string.sign_in);
        ListObjects.visibleGoneView(new View[]{contentSignIn, btnCloseContent}, contentRegister, btnExplore, btnFooter);
    }

    private void displayRegisterForm(View view) {
        updateStateLayout(VIEW_TYPE_REGISTER);

        btnSubmit.setText(R.string.register);
        ListObjects.visibleGoneView(new View[]{contentRegister, btnCloseContent}, contentSignIn, btnExplore, btnFooter);
    }

    private void initializeLayout(View rootView) {
        contentSignIn = rootView.findViewById(R.id.contentLogin);
        contentRegister = rootView.findViewById(R.id.contentRegister);
        textInputEmailIn = rootView.findViewById(R.id.et_email_sign_in);
        textInputPass = rootView.findViewById(R.id.et_password);
        btnExplore = rootView.findViewById(R.id.btn_explore);
        textInputFullName = rootView.findViewById(R.id.et_full_name);
        textInputPhone = rootView.findViewById(R.id.et_phone);
        textInputEmailReg = rootView.findViewById(R.id.et_email_register);
        textInputNewPassword = rootView.findViewById(R.id.new_password);
        textInputConfirmPassword = rootView.findViewById(R.id.confirm_password);
        btnSubmit = rootView.findViewById(R.id.btn_sign_in);
        btnFooter = rootView.findViewById(R.id.btn_footer);
        btnCloseContent = rootView.findViewById(R.id.btn_close_content);
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
