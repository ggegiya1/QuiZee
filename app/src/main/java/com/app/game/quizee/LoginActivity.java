package com.app.game.quizee;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.game.quizee.backend.PlayerManager;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlayerManager.PlayerLoggedCallback{

    private static final String TAG = "login";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUserNameField;
    private PlayerManager playerManager;

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        playerManager = PlayerManager.getInstance();
        playerManager.setLoggedCallback(this);

        // Views
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mUserNameField = (EditText) findViewById(R.id.user_name);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        playerManager.onStart();
    }


    @Override
    public void onStop(){
        super.onStop();
        playerManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.onStop();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        showProgressDialog();
        if (!validateForm()){
            return;
        }
        int i = v.getId();
        if (i == R.id.register_button) {
            playerManager.createAccount(mUserNameField.getText().toString(), mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button && validateForm()) {
            playerManager.signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

        }
        hideProgressDialog();
    }

    @Override
    public void onLogin() {
        // start game once logged in
        Log.i(TAG, "Login successful! Starting main activity");
        Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
        startActivity(intent);
    }
}

