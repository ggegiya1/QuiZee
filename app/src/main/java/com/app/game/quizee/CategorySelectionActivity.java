package com.app.game.quizee;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import me.grantland.widget.AutofitTextView;


public class CategorySelectionActivity extends AppCompatActivity implements Observer{

    private static final String TAG = "category.selection";

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    private Player player;
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    TextView score;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        player = PlayerManager.getInstance().getCurrentPlayer();
        player.clearSelectedCategories();
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
        final List<SelectableCategory> selectableCategories = new ArrayList<>();
        for (Category c: categoryManager.getAllCategoriesSortedByPrice()){
            selectableCategories.add(new SelectableCategory(c));
        }
        // select categories by price descending
        Collections.sort(selectableCategories, new Comparator<SelectableCategory>() {
            @Override
            public int compare(SelectableCategory o1, SelectableCategory o2) {
                return Double.compare(o1.getCategory().getPrice(), o2.getCategory().getPrice());
            }
        });
        final CategoryListAdapter adapterCategory = new CategoryListAdapter(this, selectableCategories);
        categoryList.setAdapter(adapterCategory);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // get category associated to the current row
                SelectableCategory category = selectableCategories.get(position);
                if(category.isSelected()) {
                    unSelectCategory(category);
                } else {
                    // instant purchase category
                    if (!player.alreadyPurchased(category.getCategory()) && category.getCategory().getPrice() > 0 && player.canBuy(category.getCategory())) {
                        instantCategoryBuyDialog(category);
                    }else if (player.alreadyPurchased(category.getCategory()) || category.getCategory().getPrice() == 0){
                        // select category if already purchased or is free
                        selectCategory(category);
                    }else {
                        // do not select when not enough points to unlock category
                        Toast.makeText(getApplicationContext(), R.string.not_enough_points_category, Toast.LENGTH_SHORT).show();
                    }
                }
                categoryList.setItemChecked(position, category.isSelected());
                adapterCategory.notifyDataSetChanged();
            }
        });
    }

    private void addPlayerToolBar(Player player){
        playerName = (TextView) findViewById(R.id.user_name);
        points = (TextView) findViewById(R.id.points);
        level = (TextView) findViewById(R.id.level);
        avatar = (ImageView) findViewById(R.id.avatar);
        score = (TextView) findViewById(R.id.score);
        updatePlayerInfo(player);

    }

    private void updatePlayerInfo(Player player){
        playerName.setText(player.getName());
        points.setText(String.format(getResources().getString(R.string.points_format), player.getPoints()));
        score.setText(String.format(getResources().getString(R.string.score_format), player.getHighestScore()));
        level.setText(String.valueOf(player.getLevel()));
        //TODO pass real image here
        avatar.setImageResource(R.drawable.ic_multi_player);
    }

    @Override
    public void update(Observable o, Object arg) {
        // update player info if changed
        updatePlayerInfo((Player)o);
    }

    private void buyAndSelectCategory(SelectableCategory category){
        player.buyCategory(category.getCategory());
        selectCategory(category);
    }

    private void selectCategory(SelectableCategory category){
        Log.i(TAG, "Selected category: " + category.getCategory());
        player.addSelectedCategory(category.getCategory());
        category.setSelected(true);
    }

    private void unSelectCategory(SelectableCategory category){
        player.removeSelectedCategory(category.getCategory());
        category.setSelected(false);
    }

    private void instantCategoryBuyDialog(final SelectableCategory category){
        AlertDialog.Builder builder = new AlertDialog.Builder(categoryList.getContext());
        builder.setMessage(category.getCategory().getName());
        builder.setTitle(R.string.buy_category);
        builder.setPositiveButton(R.string.buy, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buyAndSelectCategory(category);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unSelectCategory(category);
            }
        });
        builder.show();
    }

    private class CategoryListAdapter extends ArrayAdapter<SelectableCategory> {
        private final List<SelectableCategory> categories;
        private final Context activityContext;

        CategoryListAdapter(Activity activityContext, List<SelectableCategory> categories) {
            super(activityContext, R.layout.category_selection_list_item, categories);
            this.categories=categories;
            this.activityContext = activityContext;
        }

        @NonNull
        @Override
        public View getView(int position, View currentView, @NonNull final ViewGroup parent) {
            SelectableCategory category = getItem(position);
            LayoutInflater inflater=LayoutInflater.from(activityContext);
            ViewHolder holder;
            if(currentView == null) {
                currentView = inflater.inflate(R.layout.category_selection_list_item, null);
                holder = new ViewHolder();
                holder.categoryImage = (ImageView) currentView.findViewById(R.id.category_item_icon);
                holder.categoryName = (AutofitTextView) currentView.findViewById(R.id.category_item_name);
                holder.categoryPrice = (TextView) currentView.findViewById(R.id.category_item_price);
                currentView.setTag(holder);
            }else {
                holder = (ViewHolder)currentView.getTag();
            }
            updateItem(holder, category);
            return currentView;
        }

        private void updateItem(ViewHolder holder, SelectableCategory category){
            holder.categoryImage.setImageResource(category.getCategory().getImageId());
            holder.categoryName.setText(category.getCategory().getDisplayName());
            if (category.getCategory().getPrice() == 0) {
                holder.categoryPrice.setText("");
            }else if(player.alreadyPurchased(category.getCategory())){
                holder.categoryPrice.setText(R.string.unlocked);
                holder.categoryPrice.setTextColor(Color.GREEN);
            }else {
                holder.categoryPrice.setText(String.format(Locale.ROOT, "%d points", category.getCategory().getPrice()));
                holder.categoryPrice.setTextColor(Color.YELLOW);
            }

        }

    }

    private static class ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        TextView categoryPrice;
    }
}
