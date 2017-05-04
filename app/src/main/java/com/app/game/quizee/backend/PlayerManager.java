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
 * A singleton class, wrapper on Firebase API
 * Performs user authorisation, reading and storing user profile in the Firebase DB
 */

public class PlayerManager{

    private static final String TAG = "player.manager";

    private static PlayerManager instance;

    private FirebaseAuth mAuth;

    private Player currentPlayer;

    /**
     * Class to be called when player is logged in
     */
    private PlayerLoggedCallback loggedCallback;

    /**
     * Class to be called when players top list received
     */
    private TopListReceivedCallback topListReceivedCallback;

    /**
     * @return a player that is currently logged in
     */
    public Player getCurrentPlayer(){
        if (currentPlayer==null){
            loggedCallback.onFailure("You are not logged in");
           }
        return currentPlayer;
    }

    /**
     * This is a singleton private constructor
     */
    private PlayerManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * @return singleton instance of the PlayerManager
     */
    public static synchronized PlayerManager getInstance(){
        if (instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    /**
     * Try to get currently authenticated user
     */
    public void loginWithCurrentUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            String playerId = user.getUid();
            logInPlayer(playerId);
        }else {
            loggedCallback.onFailure("You are not logged in");
        }
    }

    /**
     * The method should be called in each activity before close or destroy
     * Save the current player
     */
    public void onStop() {
        saveCurrentPlayer();
    }

    /**
     * Create a new user in QuiZee database with username, email and password
     */
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

    /**
     * Login to an existing account with email and password
     */
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


    /**
     * Try to fetch the existing user's profile from the Firebase, or create a new one if not exists
     */
    private void logInPlayer(final String playerId, final String userName){
        Log.i(TAG, "Logging in as " + playerId);
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        playersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.child(playerId).getValue(Player.class);
                if (player == null){
                    final Player newPlayer = new Player(playerId,  userName);
                    savePlayer(newPlayer);
                    currentPlayer = newPlayer;
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

    /**
     * Fetch the existing user's profile
     */
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
    /**
     * The next 2 methods save 2 different types of players in the database
     * The current one and the one given in param
     */
    public void saveCurrentPlayer() {
        Player player = getCurrentPlayer();
        // Do not store the practice player
        if (player != null && player!=Player.defaultPlayer()){
            Log.i(TAG, "Saving player: " + player);
            final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
            playersDatabase.child(player.getId()).setValue(player);
        }
    }

    public void savePlayer(Player player) {
        Log.i(TAG, "Saving player: " + player);
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        playersDatabase.child(player.getId()).setValue(player);

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

    /**
     * Query the database to get the players by their highestscore
     */
    public void getTopPlayers(int maxTopPlayers) {
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        Query myTopPostsQuery = playersDatabase.orderByChild("highestScore").limitToLast(maxTopPlayers);
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
    /**
     * Query the database to get the players by their total score
     */
    public void getTopPlayersTotal(int maxTopPlayers) {
        final DatabaseReference playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");
        Query myTopPostsQuery = playersDatabase.orderByChild("totalratio").limitToLast(maxTopPlayers);
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

    public void logout() {
        saveCurrentPlayer();
        signOut();
    }

    /**
     * All the classes using PlayerManager to login in QuiZee app have to implement this interface
     */
    public interface PlayerLoggedCallback{
        /**
         * Callback method to be called by authentication service when successfully logged in
         */
        void onLogin();

        /**
         * Callback method to be called by authentication service if an error occurred or the login failed
         * @param message failure description
         */
        void onFailure(String message);
    }

    /**
     * All the classes using PlayerManager to read the Players from database should implement this interface
     */
    public interface TopListReceivedCallback{
        void onError(String message);
        void onItemRead(Player player);
    }
}
