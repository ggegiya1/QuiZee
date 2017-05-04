package com.app.game.quizee.layout;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Player;
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

    /**
     * A progress dialog to show when interacting with login service
     */
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


    /**
     * Create and initialise the login screen
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupAuthenticationService();
        createLoginForm();
        createLoginButtons();
    }

    private void setupAuthenticationService(){
        playerManager = PlayerManager.getInstance();
        playerManager.setLoggedCallback(this);
    }

    private void createLoginForm(){
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mUserNameField = (EditText) findViewById(R.id.user_name);
    }

    private void createLoginButtons(){
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        playerManager.loginWithCurrentUser();
    }


    @Override
    public void onStop(){
        super.onStop();
        playerManager.saveCurrentPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.saveCurrentPlayer();
    }

    /**
     * Validates that email and password fields are not empty
     * @return true is the email and password field are not empty
     */
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


    /**
     * Login or create user on click
     * @param v
     */
    @Override
    public void onClick(View v) {
        showProgressDialog();
        // special account to use for debug
        if (mUserNameField.getText().toString().equals("test")){
            playerManager.setCurrentPlayer(Player.defaultPlayer());
            onLogin();
            return;
        }
        if (!validateForm()){
            hideProgressDialog();
            return;
        }
        // login or create a new user if the form is validated
        int i = v.getId();
        if (i == R.id.register_button) {
            playerManager.createAccount(mUserNameField.getText().toString(), mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            playerManager.signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

        }
    }


    /**
     * Callback method to be called by authentication service when successfully logged in
     */
    @Override
    public void onLogin() {
        // start game once logged in
        Log.i(TAG, "Login successful! Starting main activity");
        hideProgressDialog();
        Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
        startActivity(intent);
    }

    /**
     * Callback method to be called by authentication service if an error occurred or the login failed
     * @param message failure description
     */
    @Override
    public void onFailure(String message) {
        hideProgressDialog();
        Toast.makeText(getApplicationContext(), "Authentication error: " + message, Toast.LENGTH_SHORT).show();
    }
}

