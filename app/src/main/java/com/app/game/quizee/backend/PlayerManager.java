package com.app.game.quizee.backend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by gia on 23/04/17.
 */

public class PlayerManager{

    private static final String TAG = "player.manager";

    private static PlayerManager instance;

    private FirebaseAuth mAuth;

    private Player currentPlayer;


    private PlayerLoggedCallback loggedCallback;

    private TopListReceivedCallback topListReceivedCallback;


    public Player getCurrentPlayer(){
        if (currentPlayer==null){
            loggedCallback.onFailure("You are not logged id");
           }
        return currentPlayer;
    }

    private PlayerManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized PlayerManager getInstance(){
        if (instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public void onStart() {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            String playerId = user.getUid();
            logInPlayer(playerId);
        }else {
            loggedCallback.onFailure("You are not logged id");
        }
    }


    public void onStop() {
        saveCurrentPlayer();
    }

    public void createAccount(final String userName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null){
                                String playerId = user.getUid();
                                logInPlayer(playerId, userName);
                            }else {
                                loggedCallback.onFailure("Login failed\nIncorrect user name or passwod");
                            }
                        }else {
                            loggedCallback.onFailure("Login failed\nIncorrect user name or passwod");
                        }
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null) {
                                String playerId = user.getUid();
                                logInPlayer(playerId);
                            }
                        }else {
                            loggedCallback.onFailure("Login failed\nIncorrect user name or passwod");
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
        return this.currentPlayer!=null;
    }

    private void logInPlayer(final String playerId, final String userName){
        Log.i(TAG, "Logging in as " + playerId);
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        playersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPlayer = dataSnapshot.child(playerId).getValue(Player.class);
                if (currentPlayer == null){
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

    private void logInPlayer(final String playerId){
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        playersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPlayer = dataSnapshot.child(playerId).getValue(Player.class);
                if (currentPlayer == null){
                    loggedCallback.onFailure("cannot find user in database");
                }else {
                    Log.i(TAG, "Player logged in: " + currentPlayer);
                    // pass the player to the main activity
                    loggedCallback.onLogin();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                loggedCallback.onFailure(databaseError.getMessage());
            }
        });
    }

    public void saveCurrentPlayer() {
        Player player = getCurrentPlayer();
        // do not store the practice player
        if (player != null && player!=Player.defaultPlayer()){
            Log.i(TAG, "Saving player: " + player);
            final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
            playersDatabase.child(player.getId()).setValue(player);
        }
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setLoggedCallback(PlayerLoggedCallback loggedCallback) {
        this.loggedCallback = loggedCallback;
    }

    public void setTopListReceivedCallback(TopListReceivedCallback topListReceivedCallback) {
        this.topListReceivedCallback = topListReceivedCallback;
    }

    public void getTopPlayers(int maxTopPlayers) {
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        Query myTopPostsQuery = playersDatabase.orderByChild("totalscore").limitToLast(maxTopPlayers);
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                topListReceivedCallback.onItemRead(dataSnapshot.getValue(Player.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                topListReceivedCallback.onItemRead(dataSnapshot.getValue(Player.class));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                topListReceivedCallback.onItemRead(dataSnapshot.getValue(Player.class));

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                topListReceivedCallback.onItemRead(dataSnapshot.getValue(Player.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                topListReceivedCallback.onError(databaseError.getMessage());
            }

        });

    }

    public interface PlayerLoggedCallback{
        void onLogin();
        void onFailure(String message);
    }

    public interface TopListReceivedCallback{
        void onError(String message);
        void onItemRead(Player player);
    }
}
