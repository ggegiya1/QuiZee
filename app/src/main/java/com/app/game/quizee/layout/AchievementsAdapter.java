package com.app.game.quizee.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;

import java.util.ArrayList;
import java.util.List;

//Adapter inspiré de
// http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
public class AchievementsAdapter extends ArrayAdapter<Achievement> {

    Activity context;

    public AchievementsAdapter (Activity context, List<Achievement> achievement) {
        super(context, R.layout.top_players_item_list_layout, achievement);


        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        ViewHolder holder;

        Achievement a = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.achievements_item_list_layout, null, true);
            holder = new ViewHolder();
            holder.achievementName = (TextView) convertView.findViewById(R.id.achievement_item_name);
            holder.achievementIcon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon); //TODO remove?
            holder.expGiven = (TextView) convertView.findViewById(R.id.achievement_exp_given);
            holder.goldGiven = (TextView) convertView.findViewById(R.id.achievement_gold_given);
            holder.check = (ImageView) convertView.findViewById(R.id.achievement_check);
            holder.information = (TextView) convertView.findViewById(R.id.achievement_information);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.achievement_progress);
            holder.pb.setMax(100); // range 0 .. 100
            holder.currency = (ImageView) convertView.findViewById(R.id.currency_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.achievementName.setText(a.getDesc());
        holder.information.setText(a.getInformation());
        if(a.getXP() != 0) {
            holder.expGiven.setVisibility(View.VISIBLE);
            holder.expGiven.setText(context.getString(R.string.career_and) + " " + a.getXP() + " " + context.getString(R.string.career_xp));
        } else {
            holder.expGiven.setVisibility(View.INVISIBLE);
        }
        if(a.getMoney() != 0) {
            holder.goldGiven.setVisibility(View.VISIBLE);
            holder.currency.setVisibility(View.VISIBLE);
            holder.goldGiven.setText(context.getString(R.string.career_gives) + " " + a.getMoney());
        } else {
            holder.goldGiven.setVisibility(View.INVISIBLE);
            holder.currency.setVisibility(View.INVISIBLE);
        }

        if(a.getInformation() != null) {
            holder.information.setText(a.getInformation());
            holder.check.setVisibility(View.INVISIBLE);
            holder.pb.setVisibility(View.INVISIBLE);
        }   else {
            if(a.getMaxProgress() == a.getProgress()) {
                holder.check.setVisibility(View.VISIBLE);
            }
            holder.check.setVisibility(View.INVISIBLE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.pb.setProgress((a.getProgress() * 100)/ a.getMaxProgress());
            holder.information.setText(a.getProgress() + " out of " + a.getMaxProgress());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView achievementName;
        ImageView achievementIcon;
        ImageView check;
        ImageView currency;
        TextView information;
        ProgressBar pb;
        TextView expGiven;
        TextView goldGiven;
    }
}
