package com.app.game.quizee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import com.app.game.quizee.BackEnd.Category;

import java.util.ArrayList;

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
    public static ArrayList<Category> mes_cate = new ArrayList<>();

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

        create_category();

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

    private void create_category() {
        //TO PUT IN DB
        Category category = new Category(0, "General Knowledge", 0);
        mes_cate.add(category);
        Category category1 = new Category(1, "Entertainment: Music", 200);
        mes_cate.add(category1);
        Category category2 = new Category(2, "Entertainment: Video Games", 500);
        mes_cate.add(category2);
        Category category3 = new Category(3, "Science: Computers", 300);
        mes_cate.add(category3);
        Category category4 = new Category(4, "Geography", 100);
        mes_cate.add(category4);
        Category category5 = new Category(5, "History", 150);
        mes_cate.add(category5);
        Category category6 = new Category(6, "Art", 50);
        mes_cate.add(category6);
        Category category7 = new Category(7,"Entertainment: Books",100);
        mes_cate.add(category7);
        Category category8 = new Category(8,"Entertainment: Film",50);
        mes_cate.add(category8);
        Category category9 = new Category(9,"Entertainment: Musicals & Theatres",50);
        mes_cate.add(category9);
        Category category10 = new Category(10,"Entertainment: Television",100);
        mes_cate.add(category10);
        Category category11 = new Category(11,"Entertainment: Board Games",100);
        mes_cate.add(category11);
        Category category12 = new Category(12,"Science & Nature",200);
        mes_cate.add(category12);
        Category category13 = new Category(13,"Celebrities",200);
        mes_cate.add(category13);
        Category category14 = new Category(14,"Animals",100);
        mes_cate.add(category14);
        Category category15 = new Category(15,"Politics",300);
        mes_cate.add(category15);
        Category category16 = new Category(16,"Science: Mathematics",400);
        mes_cate.add(category16);
        Category category17 = new Category(17,"Mythology",0);
        mes_cate.add(category17);
        Category category18 = new Category(18,"Sports",0);
        mes_cate.add(category18);
        Category category19 = new Category(19,"Vehicles",0);
        mes_cate.add(category19);
        Category category20 = new Category(20,"Entertainment: Comics",0);
        mes_cate.add(category20);
        Category category21 = new Category(21,"Entertainment: Japanese Anime & Manga",0);
        mes_cate.add(category21);
        Category category22 = new Category(22,"Entertainment: Cartoon & Animations",0);
        mes_cate.add(category22);
        Category category23 = new Category(23,"Science: Gadgets",0);
        mes_cate.add(category23);
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
