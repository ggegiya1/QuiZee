package com.app.game.quizee;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.BackEndManager;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import me.grantland.widget.AutofitTextView;


public class CategorySelectionActivity extends AppCompatActivity implements Observer{

    private static final String TAG = "category.selection";

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        Player player = PlayerManager.getInstance().getCurrentPlayer();
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
                Player player = PlayerManager.getInstance().getCurrentPlayer();
                if(category.isSelected()) {
                    unSelectCategory(player, category);
                } else {
                    // instant purchase category
                    if (!player.alreadyPurchased(category.getCategory()) && category.getCategory().getPrice() > 0 && player.canBuy(category.getCategory())) {
                        instantCategoryBuyDialog(player, category, view);
                    }else if (player.alreadyPurchased(category.getCategory()) || category.getCategory().getPrice() == 0){
                        // select category if already purchased or is free
                        selectCategory(player, category);
                    }else {
                        // do not select when not enough points to unlock category
                        Toast.makeText(getApplicationContext(), R.string.not_enough_points_category, Toast.LENGTH_SHORT).show();
                    }
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
        //score = (TextView) findViewById(R.id.score);
        updatePlayerInfo(player);

    }

    private void updatePlayerInfo(Player player){
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bitmap avatarBitmap = PlayerManager.getInstance().getCurrentPlayer().getAvatarBitmap(sp);

        if(avatarBitmap != null) {
            avatar.setImageBitmap(avatarBitmap);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        // update player info if changed
        updatePlayerInfo((Player)o);
    }

    private void buyAndSelectCategory(Player player, SelectableCategory category){
        player.buyCategory(category.getCategory());
        selectCategory(player, category);
    }

    private void selectCategory(Player player, SelectableCategory category){
        Log.i(TAG, "Selected category: " + category.getCategory());
        player.addSelectedCategory(category.getCategory());
        category.setSelected(true);
    }

    private void unSelectCategory(Player player, SelectableCategory category){
        player.removeSelectedCategory(category.getCategory());
        category.setSelected(false);
    }

    private void instantCategoryBuyDialog(final Player player, final SelectableCategory category, final View rowView){
        AlertDialog.Builder builder = new AlertDialog.Builder(categoryList.getContext());
        final ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        builder.setMessage(category.getCategory().getName());
        builder.setTitle(R.string.buy_category);
        builder.setPositiveButton(R.string.buy, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buyAndSelectCategory(player, category);
                updateItem(viewHolder, category);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unSelectCategory(player, category);
                updateItem(viewHolder, category);
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
                holder.selectionToggle = (CheckBox) currentView.findViewById(R.id.radio_button_selection_toggle);
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
        if (category.getCategory().getPrice() == 0) {
            holder.categoryPrice.setVisibility(View.INVISIBLE);
        }else if(PlayerManager.getInstance().getCurrentPlayer().alreadyPurchased(category.getCategory())){
            holder.categoryPrice.setText(R.string.unlocked);
            holder.categoryPrice.setCompoundDrawables(null, null, null, null);
            holder.categoryPrice.setVisibility(View.VISIBLE);
            holder.categoryPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else {
            holder.categoryPrice.setVisibility(View.VISIBLE);
            holder.categoryPrice.setCompoundDrawables(null, null, getDrawable(R.drawable.ic_currency), null);
            holder.categoryPrice.setText(String.valueOf(category.getCategory().getPrice()));
        }

    }

    private static class ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        TextView categoryPrice;
        CheckBox selectionToggle;
    }

    //on settings button clicked
    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}
