package com.app.game.quizee.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.game.quizee.R;

public class CareerFragment extends Fragment {

    ListView statsList;
    ListView achievementsList;

    public CareerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_achievements, container, false);

        achievementsList = (ListView) ll.findViewById(R.id.achievements_list);
        statsList = (ListView) ll.findViewById(R.id.stats_list);

        //TODO aller chercher les informations dachievement programmaticallement

        //AchievementsAdapter adapter = new AchievementsAdapter(getActivity(),  achievements);
        //statsList.setAdapter(adapter);
        //achievementsList.setAdapter(adapter);
        return ll;
    }
}
