package com.app.game.quizee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CategorySelectionActivity extends AppCompatActivity {

    ListView categoryList;

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

        categoryList = (ListView) findViewById(R.id.category_list);


        //TODO obtaint category programmatically
        int[] categoryImageId = new int[] {R.drawable.ic_geography, R.drawable.ic_computer, R.drawable.ic_art,
                R.drawable.ic_general_knowledge, R.drawable.ic_art, R.drawable.ic_history};
        String[] categories = new String[] {"Geography", "Computers", "Art","Art", "Bob", "History"};

        ContactAdapter adapterCategory = new ContactAdapter(this, categories, categoryImageId);
        categoryList.setAdapter(adapterCategory);

    }

    //contien temporairement un chekedTextView pour le réutiliser
    private static class ViewHolder {
        CheckedTextView categoryListItem;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class ContactAdapter extends ArrayAdapter<String> {

        String[] itemname;
        int[] imgid;
        Activity context;

        public ContactAdapter (Activity context, String[] itemname, int[] imgid) {
            super(context, R.layout.contacts_item_list_layout, itemname);
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            holder.categoryListItem.setVisibility(View.VISIBLE);
            holder.categoryListItem.setText(itemname[position]);
            holder.categoryListItem.setCompoundDrawablesWithIntrinsicBounds(getDrawable(imgid[position]), null, null, null);
            holder.categoryListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
