package com.app.game.quizee.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

public class AchievementsFragment extends Fragment {

    ListView achievementsList;
    /**
     * Required empty public constructor
     */
    public AchievementsFragment() {
    }

    /**
     *  Method for sorting the achievements. The closest of completing on top, completed at the end
     */
    public static Achievement[] sortAchivements(Achievement[] arr, Player player){

        for (int i = 0; i < arr.length - 1; i++){
            int index = i;
            for (int j = i +1; j < arr.length; j++) {
                if (arr[j].getProg(player) < arr[index].getProg(player))
                    index = j;
            }
            Achievement small = arr[index];
            arr[index] = arr[i];
            arr[i] = small;

        }
        for (int i = 0; i < arr.length - 1; i++){
            int index = i;
            for (int j = i +1; j < arr.length; j++) {
                if (arr[j].getProg(player) > arr[index].getProg(player) && arr[j].getProg(player) < 100)
                    index = j;
            }
            Achievement big = arr[index];
            arr[index] = arr[i];
            arr[i] = big;

        }
        return arr;

    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_achievements, container, false);
        achievementsList = (ListView) rl.findViewById(R.id.achievements_list);

        // Creates the achievement array and sorts it
        final Achievement[] achievements = Achievement.values();
        final Player current_player = PlayerManager.getInstance().getCurrentPlayer();
        sortAchivements(achievements, current_player);

        // Creates the adapter for the achievement page
        achievementsList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return achievements.length;
            }
            @Override
            public Object getItem(int position) {
                return null;
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }

            /**
             * Manipulation fo the View elements for the achievement page
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                if (convertView == null)
                    convertView = inflater.inflate(R.layout.achievements_item_list_layout, parent, false);

                TextView name = (TextView) convertView.findViewById(R.id.achievement_item_name);
                TextView gold = (TextView) convertView.findViewById(R.id.achievement_gold_given);
                TextView xp = (TextView) convertView.findViewById(R.id.achievement_exp_given);
                TextView info = (TextView) convertView.findViewById(R.id.achievement_information);
                ImageView check = (ImageView) convertView.findViewById(R.id.achievement_check);
                ProgressBar bar = (ProgressBar) convertView.findViewById(R.id.achievement_progress);

                //Setters
                name.setText(achievements[position].getDesc());
                gold.setText(String.valueOf("+ " + achievements[position].getMoney()));
                xp.setText(String.valueOf("+ "+ achievements[position].getXP()) + " Exp");
                info.setText(achievements[position].getInformation());

                //Sets the done/not done icon
                if (achievements[position].getProg(current_player) >= 100){
                    check.setImageResource(R.drawable.ic_check);
                }else{
                    check.setImageResource(R.drawable.ic_not_done);
                }
                bar.setProgress(achievements[position].getProg(current_player));
                return convertView;
            }

        });

        return rl;
    }




}
