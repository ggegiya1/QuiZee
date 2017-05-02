package com.app.game.quizee.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.MusicService;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import me.grantland.widget.AutofitTextView;

/**
 * Created by ggegiya1 on 2017-05-01.
 */

public class FavoriteCategorySelectionActivity extends AppCompatActivity implements Observer {


    private static final String TAG = "category.selection";

    CategoryListAdapter adapterCategory;
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    ImageView avatar;
    List<SelectableCategory> selectableCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        selectableCategories = new ArrayList<>();
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        player.clearSelectedCategories();
        player.addObserver(this);
        addPlayerToolBar(player);
        addStartButton(player);
        addCategoriesList();
        setupUnselectButton();
    }

    private void addStartButton(final Player player){
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SelectableCategory sc: selectableCategories){
                    if (sc.isSelected()){
                        player.addSelectedCategory(sc.getCategory());
                    }
                }
                if (player.getCategoriesSelected().size()>0) {
                    Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.no_category_selected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCategoriesList(){
        categoryList = (ListView) findViewById(R.id.category_list);
        categoryList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        for (Category c: PlayerManager.getInstance().getCurrentPlayer().getCategoriesFavorites()){
            selectableCategories.add(new SelectableCategory(c));
        }
        // select categories by popularity descending
        Collections.sort(selectableCategories, new Comparator<SelectableCategory>() {
            @Override
            public int compare(SelectableCategory o1, SelectableCategory o2) {
                return Double.compare(o2.getCategory().getPopularity(), o1.getCategory().getPopularity());
            }
        });
        adapterCategory = new CategoryListAdapter(this, selectableCategories);
        categoryList.setAdapter(adapterCategory);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // get category associated to the current row
                SelectableCategory category = selectableCategories.get(position);
                if(category.isSelected()) {
                    category.unSelect();
                } else {
                    category.select();
                }
                adapterCategory.notifyDataSetChanged();
            }
        });
    }

    private void addPlayerToolBar(Player player){
        playerName = (TextView) findViewById(R.id.user_name);
        points = (TextView) findViewById(R.id.points);
        level = (TextView) findViewById(R.id.level);
        avatar = (ImageView) findViewById(R.id.avatar);
        updatePlayerInfo(player);

    }

    private void updatePlayerInfo(Player player){
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
        Bitmap avatarBitmap = PlayerManager.getInstance().getCurrentPlayer().avatarBitmap();
        if(avatarBitmap != null) {
            avatar.setImageBitmap(avatarBitmap);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        // update player info if changed
        updatePlayerInfo((Player)o);
    }


    private class CategoryListAdapter extends ArrayAdapter<SelectableCategory> {
        private final Context activityContext;

        CategoryListAdapter(Activity activityContext, List<SelectableCategory> categories) {
            super(activityContext, R.layout.category_selection_list_item, categories);
            this.activityContext = activityContext;
        }

        @NonNull
        @Override
        public View getView(int position, View currentView, @NonNull final ViewGroup parent) {
            SelectableCategory category = getItem(position);
            LayoutInflater inflater=LayoutInflater.from(activityContext);
            ViewHolder holder;
            if(currentView == null) {
                currentView = inflater.inflate(R.layout.fav_category_selection_list_item, null);
                holder = new ViewHolder();
                holder.categoryImage = (ImageView) currentView.findViewById(R.id.fav_category_item_icon);
                holder.categoryName = (AutofitTextView) currentView.findViewById(R.id.fav_category_item_name);
                holder.selectionToggle = (CheckBox) currentView.findViewById(R.id.fav_category_selection_toggle);
                currentView.setTag(holder);
            }else {
                holder = (ViewHolder)currentView.getTag();
            }
            updateItem(holder, category);
            return currentView;
        }
    }

    private void updateItem(ViewHolder holder, SelectableCategory category){
        holder.selectionToggle.setChecked(category.isSelected());
        holder.categoryImage.setImageResource(category.getCategory().getImageId());
        holder.categoryName.setText(category.getCategory().getDisplayName());
    }

    private static class ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        CheckBox selectionToggle;
    }

    public void setupUnselectButton() {
        Button unselect = (Button) findViewById(R.id.unselectButton);
        unselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SelectableCategory sc: selectableCategories){
                    sc.unSelect();
                }
                adapterCategory.notifyDataSetChanged();
            }
        });
    }



    @Override
    protected void onPause() {
        MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }

    @Override
    protected void onResume() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onResume();
    }
}
