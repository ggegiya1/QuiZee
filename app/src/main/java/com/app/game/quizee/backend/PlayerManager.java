package com.app.game.quizee.backend;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by gia on 23/04/17.
 */

public class PlayerManager{

    private static final String TAG = "player.manager";

    private static final String anonymousName = "Anonymous";

    private static PlayerManager instance;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser firebaseUser;

    private DatabaseReference playersDatabase;

    private Player currentPlayer;

    private boolean loggedIn;


    private PlayerLoggedCallback loggedCallback;


    public Player getCurrentPlayer(){
        // FIXME this is for test only
        // need to return actual value
        if (currentPlayer==null){
            currentPlayer = Player.defaultPlayer();
           }
        return currentPlayer;
    }

    private PlayerManager() {
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser!=null) {
                    loggedIn = true;
                    String playerId = firebaseUser.getUid();
                    notifyObserversWhenPlayerChanges(playerId);
                }else {
                    loggedIn = false;
                }
            }
        };

    }

    public static synchronized PlayerManager getInstance(){
        if (instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }


    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        saveCurrentPlayer();
    }

    public void createAccount(final String userName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loggedIn = true;
                        updateUserName(userName);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loggedCallback.onFailure(e.getMessage());
            }
        });
    }

    public void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           loggedIn = true;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loggedCallback.onFailure(e.getMessage());
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    private void notifyObserversWhenPlayerChanges(final String playerId){
        playersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPlayer = dataSnapshot.child(playerId).getValue(Player.class);
                if (currentPlayer == null){
                    String userName = firebaseUser.getDisplayName() == null? anonymousName: firebaseUser.getDisplayName();
                    currentPlayer = new Player(playerId,  userName);
                    playersDatabase.child(playerId).setValue(currentPlayer);
                }
                Log.i(TAG, "Player logged in: " + currentPlayer);
                // pass the player to the main activity
                loggedCallback.onLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loggedCallback.onFailure(databaseError.getMessage());
            }
        });
    }

    private void updateUserName(String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public void saveCurrentPlayer() {
        Player player = getCurrentPlayer();
        if (player != null){
            Log.i(TAG, "Saving player: " + player);
            playersDatabase.child(player.getId()).setValue(player);
        }
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.loggedIn = true;
    }

    public void setLoggedCallback(PlayerLoggedCallback loggedCallback) {
        this.loggedCallback = loggedCallback;
    }

    public interface PlayerLoggedCallback{
        void onLogin();
        void onFailure(String message);
    }
}
