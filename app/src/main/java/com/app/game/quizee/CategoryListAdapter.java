package com.app.game.quizee;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.util.AutofitTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gia on 18/04/17.
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private final List<SelectableCategory> categories;
    private final Context activityContext;
    private final Player player;

    CategoryListAdapter(Activity activityContext, List<Category> categories, Player player) {
        super(activityContext, R.layout.category_selection_list_item, categories);
        this.categories=new ArrayList<>();
        for (Category c: categories){
            this.categories.add(new SelectableCategory(c));
        }
        this.activityContext = activityContext;
        this.player = player;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
        SelectableCategory category = categories.get(position);
        LayoutInflater inflater=LayoutInflater.from(activityContext);
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.category_selection_list_item, null);
            holder = new ViewHolder();
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.category_item_icon);
            holder.categoryName = (AutofitTextView) convertView.findViewById(R.id.category_item_name);
            holder.categoryPrice = (TextView) convertView.findViewById(R.id.category_item_price);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.select_category_item);
            holder.checkBox.setTag(categories.get(position));
            holder.checkBox.setOnCheckedChangeListener(categoryRowCheckBoxListener(holder, activityContext));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
            holder.checkBox.setTag(categories.get(position));
        }
        holder.categoryImage.setImageResource(category.getImageId());
        holder.categoryName.setText(category.getDisplayName());
        holder.categoryPrice.setText(String.valueOf(category.getPrice()));
        holder.checkBox.setChecked(category.isSelected());
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener categoryRowCheckBoxListener(final CategoryListAdapter.ViewHolder holder, final Context context){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                // get category associated to the current row
                SelectableCategory category = (SelectableCategory)holder.checkBox.getTag();
                if(isChecked) {
                    // instant purchase category
                    if (!player.hasCategory(category) && category.getPrice() > 0 && player.canBuy(category)) {
                        instantCategoryBuyDialog(category, holder.checkBox);
                    }else if (player.hasCategory(category) || category.getPrice() == 0){
                        // select category if already purchased or is free
                        selectCategory(category);
                    }else {
                        // do not select when not enough points to unlock category
                        holder.checkBox.setChecked(false);
                        Toast.makeText(context, R.string.not_enough_points_category, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    unSelectCategory(category);
                }
            }
        };
    }

    private void instantCategoryBuyDialog(final SelectableCategory category, final CheckBox checkBox){
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setMessage(category.getName());
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
                checkBox.setChecked(false);
            }
        });
        builder.show();
    }

    private void buyAndSelectCategory(SelectableCategory category){
        player.buyCategory(category);
        selectCategory(category);
    }

    private void selectCategory(SelectableCategory category){
        player.addSelectedCategory(category);
        category.setSelected(true);
    }

    private void unSelectCategory(SelectableCategory category){
        player.removeSelectedCategory(category);
        category.setSelected(false);
    }

    private static class ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        TextView categoryPrice;
        CheckBox checkBox;
    }
}
