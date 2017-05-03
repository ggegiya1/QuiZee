package com.app.game.quizee.layout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class CategorySelectionActivity extends AppCompatActivity implements Observer{
    private static final String TAG = "category.selection";
    private final CategoryManager categoryManager = CategoryManager.getInstance();
    CategoryListAdapter adapterCategory;
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    ImageView avatar;
    List<SelectableCategory> selectableCategories;

    /**
     * Creates the Category selection view
     */
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

    /**
     * Get the current player and clear the selected categories
     */
    @Override
    protected void onStart() {
        super.onStart();
        // redraw the list according the current player's state
        PlayerManager.getInstance().getCurrentPlayer().clearSelectedCategories();
        adapterCategory.notifyDataSetChanged();
    }

    /**
     * Start new single player with selected questions once start button is pressed
     */
    private void addStartButton(final Player player){
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected questions
                for (SelectableCategory sc: selectableCategories){
                    if (sc.isSelected()){
                        player.addSelectedCategory(sc.getCategory());
                    }
                }
                // start the game with the selected questions
                if (player.getCategoriesSelected().size()>0) {
                    Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.no_category_selected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Visualise the categories list
     */
    private void addCategoriesList(){
        categoryList = (ListView) findViewById(R.id.category_list);
        categoryList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        for (Category c: categoryManager.getAllCategories()){
            selectableCategories.add(new SelectableCategory(c));
        }
        // select categories by price descending
        Collections.sort(selectableCategories, new Comparator<SelectableCategory>() {
            @Override
            public int compare(SelectableCategory o1, SelectableCategory o2) {
                return Double.compare(o1.getCategory().getPrice(), o2.getCategory().getPrice());
            }
        });
         adapterCategory = new CategoryListAdapter(this, selectableCategories);
        categoryList.setAdapter(adapterCategory);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // get category associated to the current row
                SelectableCategory category = selectableCategories.get(position);
                Player player = PlayerManager.getInstance().getCurrentPlayer();
                if(category.isSelected()) {
                    category.unSelect();
                } else {
                    // instant purchase category
                    if (purchaseAllowed(player, category.getCategory())) {
                        instantCategoryBuyDialog(player, category, view);
                    }else if (selectAllowed(player, category.getCategory())){
                        // select category if already purchased or is free
                        category.select();
                    }else {
                        // do not select when not enough points to unlock category
                        Toast.makeText(getApplicationContext(), R.string.not_enough_points_category, Toast.LENGTH_SHORT).show();
                    }
                }
                adapterCategory.notifyDataSetChanged();
            }
        });
    }

    /**
     * Tells if the player can purchase the category
     * @return true if the category can be bought
     */
    private boolean purchaseAllowed(Player player, Category category){
        return !player.alreadyPurchased(category) && category.getPrice() > 0 && player.canBuy(category);
    }

    /**
     * Tells if the player can select the category
     * @return true if the category can be selected
     */
    private boolean selectAllowed(Player player, Category category){
        return player.alreadyPurchased(category) || category.getPrice() == 0;
    }

    /**
     * Adds the player to the toolbar
     */
    private void addPlayerToolBar(Player player){
        playerName = (TextView) findViewById(R.id.user_name);
        points = (TextView) findViewById(R.id.points);
        level = (TextView) findViewById(R.id.level_main);
        avatar = (ImageView) findViewById(R.id.avatar);
        updatePlayerInfo(player);

    }

    /**
     * Updates the toolbar to show the player info
     */
    private void updatePlayerInfo(Player player){
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
        Bitmap avatarBitmap = PlayerManager.getInstance().getCurrentPlayer().avatarBitmap();
        if(avatarBitmap != null) {
            avatar.setImageBitmap(avatarBitmap);
        }
    }

    /**
     * Update an unique user info if it changed
     */
    @Override
    public void update(Observable o, Object arg) {
        updatePlayerInfo((Player)o);
    }

    /**
     * Buy a category and select it
     */
    private void buyAndSelectCategory(Player player, SelectableCategory category){
        player.buyCategory(category.getCategory());
        category.select();
    }

    /**
     * Builds the instant buy dialog and shows it
     */
    private void instantCategoryBuyDialog(final Player player, final SelectableCategory category, final View rowView){
        AlertDialog.Builder builder = new AlertDialog.Builder(categoryList.getContext());
        final ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        builder.setMessage(category.getCategory().getName());
        builder.setTitle(R.string.buy_category);
        builder.setPositiveButton(R.string.buy, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buyAndSelectCategory(player, category);
                adapterCategory.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category.unSelect();
                updateItem(viewHolder, category);
            }
        });
        builder.show();
    }

    /**
     * An adapter for the category listview
     */
    private class CategoryListAdapter extends ArrayAdapter<SelectableCategory> {
        private final Context activityContext;

        /**
         * Constructor
         */
        CategoryListAdapter(Activity activityContext, List<SelectableCategory> categories) {
            super(activityContext, R.layout.category_selection_list_item, categories);
            this.activityContext = activityContext;
        }

        /**
         * Returns a view to be used as a row
         */
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

    /**
     * Updates a row regarding if the category was purchased or if it's available
     */
    private void updateItem(ViewHolder holder, SelectableCategory category){
        holder.selectionToggle.setChecked(category.isSelected());
        holder.categoryImage.setImageResource(category.getCategory().getImageId());
        holder.categoryName.setText(category.getCategory().getDisplayName());
        if (category.getCategory().getPrice() == 0) {
            //Category is free
            holder.categoryPrice.setVisibility(View.INVISIBLE);
        }else if(PlayerManager.getInstance().getCurrentPlayer().alreadyPurchased(category.getCategory())){
            //Player already purchased category
            holder.categoryPrice.setText(R.string.unlocked);
            holder.categoryPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.categoryPrice.setVisibility(View.VISIBLE);
            holder.categoryPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if(PlayerManager.getInstance().getCurrentPlayer().canBuy(category.getCategory())){
            //Player CAN buy category
            holder.categoryPrice.setVisibility(View.VISIBLE);
            holder.categoryPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.mipmap.ic_coins), null);
            holder.categoryPrice.setText(String.valueOf(category.getCategory().getPrice()));
            holder.categoryPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            //Player CANT buy category
            holder.categoryPrice.setVisibility(View.VISIBLE);
            holder.categoryPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.mipmap.ic_coins), null);
            holder.categoryPrice.setText(String.valueOf(category.getCategory().getPrice()));
            holder.categoryPrice.setTextColor(Color.RED);
        }

    }

    /**
     * Holds a view to be recycled for better performance
     */
    private static class ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        TextView categoryPrice;
        CheckBox selectionToggle;
    }

    /**
     * Setups the unselect button for the categories selection
     */
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

    /**
     * Pauses music when the activity pauses
     */
    @Override
    protected void onPause() {
        MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }

    /**
     * Resumes music when activity resumes
     */
    @Override
    protected void onResume() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onResume();
    }
}
