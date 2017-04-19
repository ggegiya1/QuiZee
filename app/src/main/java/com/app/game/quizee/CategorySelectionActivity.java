package com.app.game.quizee;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class CategorySelectionActivity extends AppCompatActivity implements Observer{

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    private Player player;
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        Bundle bundle = getIntent().getExtras();
        player = (Player)bundle.getSerializable("player");
        player.addObserver(this);
        addPlayerToolBar(player);
        addStartButton(player);
        addCategoriesList();
    }

    private void addStartButton(final Player player){
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("player", player);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

    private void addCategoriesList(){
        categoryList = (ListView) findViewById(R.id.category_list);
        categoryList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        CategoryListAdapter adapterCategory = new CategoryListAdapter(this, categoryManager.getAllCategoriesSortedByPrice(), player);
        categoryList.setAdapter(adapterCategory);
    }

    private void addPlayerToolBar(Player player){
        playerName = (TextView) findViewById(R.id.name);
        points = (TextView) findViewById(R.id.currency);
        level = (TextView) findViewById(R.id.level);
        avatar = (ImageView) findViewById(R.id.avatar);
        playerName.setText(player.getName());
        level.setText(String.valueOf(player.getLevel()));
        points.setText(String.valueOf(player.getPointsEarned()));
        //TODO pass real image here
        avatar.setImageResource(R.drawable.ic_multi_player);
    }

    @Override
    public void update(Observable o, Object arg) {
        points.setText(String.valueOf(((Player)o).getPointsEarned()));
    }
}
