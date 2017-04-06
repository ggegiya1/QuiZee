package com.app.game.quizee;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CareerDetails extends AppCompatActivity {

    TextView detailsTitle;
    String extra;
    ListView detailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_details);

        extra = getIntent().getStringExtra("career details");
        detailsTitle = (TextView) findViewById(R.id.career_details_title);
        detailsTitle.setText(extra);
        detailsList = (ListView) findViewById(R.id.details_list);

        String[] stats = new String[] {"favorite mode", "Games played", "Questions answered"};

        if(extra == "Statistics") {

        }





        //TODO aller chercher les informations dachievement programmaticallement

        int[] detailsImageId = new int[] {R.drawable.ic_contacts, R.drawable.ic_multi_player, R.drawable.ic_art, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_notifications_black_24dp};
        boolean[] detailsCompletion = new boolean[] {false, false, true, false};
        boolean[] nominal = new boolean[] {true, true, false, true};
        String[] detailsString = new String[] {"favorite mode", "Games played", "Questions answered", "favorite category"};
        int[] progression = new int[] {10, 60, 70, 90};
        String[] detailsNominalString = new String[] {"practice", "15", "150", "Video Games"};


        DetailsAdapter adapter = new DetailsAdapter(this, detailsString, detailsImageId, progression,nominal, detailsCompletion, detailsNominalString);
        detailsList.setAdapter(adapter);
    }

    private static class ViewHolder {
        TextView achievementName;
        ImageView achievementIcon;
        ImageView check;
        TextView nominalInformation;
        ProgressBar pb;
        TextView expGiven;
        TextView goldGiven;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class DetailsAdapter extends ArrayAdapter<String> {

        String[] detailsName;
        int[] detailsImageId;
        int[] progression;
        Activity context;
        boolean[] nominal;
        boolean[] completed;

        public DetailsAdapter (Activity context, String[] itemname, int[] imgid, int[] progress, boolean[] nomin, boolean[] complete, String[] nomDetails) {
            super(context, R.layout.contacts_item_list_layout, itemname);

            this.detailsName = itemname;
            this.context=context;
            this.detailsImageId=imgid;
            this.progression=progress;
            this.nominal=nomin;
            this.completed=complete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.achievements_item_list_layout, null, true);
                holder = new ViewHolder();
                holder.achievementName = (TextView) convertView.findViewById(R.id.achievement_item_name);
                holder.achievementIcon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon);
                holder.expGiven = (TextView) convertView.findViewById(R.id.achievement_exp_given);
                holder.goldGiven = (TextView) convertView.findViewById(R.id.achievement_gold_given);
                holder.check = (ImageView) convertView.findViewById(R.id.achievement_check);
                holder.nominalInformation = (TextView) findViewById(R.id.achievement_information);
                holder.pb = (ProgressBar) convertView.findViewById(R.id.achievement_progress);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.achievementName.setText(detailsName[position]);

            return convertView;
        }
    }
}
