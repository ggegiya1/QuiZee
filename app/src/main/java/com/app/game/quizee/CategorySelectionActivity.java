package com.app.game.quizee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import com.app.game.quizee.BackEnd.Category;

import java.util.ArrayList;
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

        create_category();

        Music = (CheckedTextView) findViewById(R.id.button_music);
        Art = (CheckedTextView) findViewById(R.id.button_art);
        Computers = (CheckedTextView) findViewById(R.id.button_computers);
        History = (CheckedTextView) findViewById(R.id.button_history);
        Geography = (CheckedTextView) findViewById(R.id.button_geography);
        GeneralKnowledge = (CheckedTextView) findViewById(R.id.button_knowledge);
        VideoGames = (CheckedTextView) findViewById(R.id.button_video_games);
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
