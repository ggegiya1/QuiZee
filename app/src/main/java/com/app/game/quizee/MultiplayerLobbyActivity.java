package com.app.game.quizee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MultiplayerLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);
    }

    //on play button pressed
    public void multiPlay(View v) {
        Intent intent = new Intent(getApplicationContext(), MultiplayerQuestionActivity.class);
        startActivity(intent);
    }


}
