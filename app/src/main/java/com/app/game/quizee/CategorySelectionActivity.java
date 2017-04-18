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


public class CategorySelectionActivity extends AppCompatActivity {

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    private Player player;
    ListView categoryList;
    TextView playerName;
    TextView level;
    TextView points;
    ImageView avatar;

    List<Category> selectedCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        Bundle bundle = getIntent().getExtras();
        player = (Player)bundle.getSerializable("player");
        addPlayerToolBar();
        addStartButton();
        addCategoriesList();
        selectedCategories = new ArrayList<>();
    }

    private void addStartButton(){
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addCategoriesList(){
        categoryList = (ListView) findViewById(R.id.category_list);
        CategoryListAdapter adapterCategory = new CategoryListAdapter(this, categoryManager.getAllCategoriesSortedByPrice());
        categoryList.setAdapter(adapterCategory);
    }

    private void addPlayerToolBar(){
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




    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class CategoryListAdapter extends ArrayAdapter<Category> {

        private final List<Category> categories;
        private final Context context;

        private class ViewHolder{
            ImageView categoryImage;
            TextView categoryName;
            TextView categoryPrice;
            CheckBox checkBox;
        }
        CategoryListAdapter(Activity context, List<Category> categories) {
            super(context, R.layout.category_selection_list_item, categories);
            this.categories=categories;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final Category category = categories.get(position);
            View view;
            LayoutInflater inflater=LayoutInflater.from(context);
            if(convertView == null) {
                view = inflater.inflate(R.layout.category_selection_list_item, null);
                final ViewHolder holder = new ViewHolder();
                holder.categoryImage = (ImageView) view.findViewById(R.id.category_item_icon);
                holder.categoryName = (TextView) view.findViewById(R.id.category_item_name);
                holder.categoryPrice = (TextView) view.findViewById(R.id.category_item_price);
                holder.checkBox = (CheckBox) view.findViewById(R.id.select_category_item);
                holder.checkBox.setTag(category);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                        Category categoryCurrent = (Category)holder.checkBox.getTag();
                        if(isChecked) {
                            // instant purchase category
                            if (!player.hasCategory(categoryCurrent) && categoryCurrent.getPrice() > 0 && player.canBuy(categoryCurrent)) {
                                instantCategoryBuyDialog(categoryCurrent, holder.checkBox);
                            }else if (player.hasCategory(categoryCurrent) || categoryCurrent.getPrice() == 0){
                                // select category if already purchased or is free
                                categoryCurrent.setChecked(true);
                            }else {
                                categoryCurrent.setChecked(false);
                                holder.checkBox.setChecked(false);
                                Toast.makeText(context, "Earn more points to buy category", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            categoryCurrent.setChecked(false);
                        }
                    }
                });
                view.setTag(holder);
            }else {
                view = convertView;
                ((ViewHolder)view.getTag()).checkBox.setTag(category);
            }
            ViewHolder holder = (ViewHolder)view.getTag();
            holder.categoryImage.setImageResource(category.getImageId());
            holder.categoryName.setText(category.getName());
            holder.categoryPrice.setText(String.valueOf(category.getPrice()));
            holder.checkBox.setChecked(category.isChecked());
            return view;
        }
    }

    void buyCategory(Category category){
        player.buyCategory(category);
        points.setText(String.valueOf(player.getPointsEarned()));

    }

    void instantCategoryBuyDialog(final Category category, final CheckBox checkBox){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(category.getName());
        builder.setTitle(R.string.buy_category);
        builder.setPositiveButton(R.string.buy, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buyCategory(category);
                category.setChecked(true);
                checkBox.setChecked(true);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category.setChecked(false);
                checkBox.setChecked(false);
            }
        });
        builder.show();
    }

}
