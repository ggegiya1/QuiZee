package com.app.game.quizee;
import android.app.Activity;
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
import android.widget.CheckedTextView;

import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Player;

import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class CategorySelectionActivity extends AppCompatActivity {

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    private Player player;
    ListView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        addStartButton();
        addCategoriesList();
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

    //contien temporairement un chekedTextView pour le réutiliser
    private static class ViewHolder {
        CheckedTextView categoryListItem;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class CategoryListAdapter extends ArrayAdapter<Category> {

        List<Category> categories;
        Activity context;

        public CategoryListAdapter(Activity context, List<Category> categories) {
            super(context, R.layout.contacts_item_list_layout, categories);
            this.context=context;
            this.categories=categories;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            final ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.category_selection_list_item, null, true);
                holder = new ViewHolder();
                holder.categoryListItem = (CheckedTextView) convertView.findViewById(R.id.category_item_list);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Category category = categories.get(position);
            holder.categoryListItem.setVisibility(View.VISIBLE);
            holder.categoryListItem.setText(category.getName() + "   " + category.getPrice());
            holder.categoryListItem.setCompoundDrawablesWithIntrinsicBounds(getDrawable(category.getImageId()), null, null, null);
            holder.categoryListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (category.getPrice() > 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = getLayoutInflater().inflate(R.layout.instant_buy_category_dialog, null);
                        TextView categoryName = (TextView) view.findViewById(R.id.instant_buy_category_name);
                        TextView categoryPrice = (TextView) view.findViewById(R.id.instant_buy_category_price);
                        Button buyCategoryButton = (Button) view.findViewById(R.id.instant_buy_category_button);
                        categoryName.setText(category.getName());
                        categoryPrice.setText(String.valueOf(category.getPrice()));
                        buyCategoryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO add buy component
                            }
                        });
                        builder.setView(view);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    if(holder.categoryListItem.isChecked()) {
                        holder.categoryListItem.setChecked(false);
                    }   else {
                        holder.categoryListItem.setChecked(true);
                    }
                }
            }
            );
            return convertView;
        }
    }


}
