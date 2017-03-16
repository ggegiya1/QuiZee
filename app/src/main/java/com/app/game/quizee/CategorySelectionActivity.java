package com.app.game.quizee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

/**
 * Created by ggegiya1 on 2/16/17.
 */

public class CategorySelectionActivity extends AppCompatActivity implements View.OnClickListener{

    CheckedTextView Music;
    CheckedTextView VideoGames;
    CheckedTextView Computers;
    CheckedTextView Geography;
    CheckedTextView Art;
    CheckedTextView GeneralKnowledge;
    CheckedTextView History;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);

        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                startActivity(intent);
            }
        });

        Music = (CheckedTextView) findViewById(R.id.button_music);
        Art = (CheckedTextView) findViewById(R.id.button_art);
        Computers = (CheckedTextView) findViewById(R.id.button_computers);
        History = (CheckedTextView) findViewById(R.id.button_history);
        Geography = (CheckedTextView) findViewById(R.id.button_geography);
        GeneralKnowledge = (CheckedTextView) findViewById(R.id.button_knowledge);
        VideoGames = (CheckedTextView) findViewById(R.id.button_video_games);

        Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Music.isChecked())
                    Music.setChecked(false);
                else
                    Music.setChecked(true);

            }
        });

        Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Art.isChecked())
                    Art.setChecked(false);
                else
                    Art.setChecked(true);

            }
        });

        Computers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Computers.isChecked())
                    Computers.setChecked(false);
                else
                    Computers.setChecked(true);

            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (History.isChecked())
                    History.setChecked(false);
                else
                    History.setChecked(true);

            }
        });

        Geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Geography.isChecked())
                    Geography.setChecked(false);
                else
                    Geography.setChecked(true);

            }
        });

        GeneralKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GeneralKnowledge.isChecked())
                    GeneralKnowledge.setChecked(false);
                else
                    GeneralKnowledge.setChecked(true);

            }
        });
        VideoGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoGames.isChecked())
                    VideoGames.setChecked(false);
                else
                    VideoGames.setChecked(true);

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void Play(View v) {
        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        intent.putExtra("Computers", Computers.isChecked());
        intent.putExtra("History", History.isChecked());
        intent.putExtra("Music", Music.isChecked());
        intent.putExtra("VideoGames", VideoGames.isChecked());
        intent.putExtra("Art", Art.isChecked());
        intent.putExtra("GeneralKnowledge", GeneralKnowledge.isChecked());
        intent.putExtra("Geography", Geography.isChecked());
        intent.putExtra("mode", "categoriesPlay");
    }
}
