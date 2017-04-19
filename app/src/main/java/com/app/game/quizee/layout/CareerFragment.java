package com.app.game.quizee.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.game.quizee.AchievementsAdapter;
import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;

import java.util.ArrayList;

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
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_career, container, false);

        achievementsList = (ListView) ll.findViewById(R.id.achievements_list);
        statsList = (ListView) ll.findViewById(R.id.stats_list);

        //TODO aller chercher les informations dachievement programmaticallement

        ArrayList<Achievement> achievements = new ArrayList<Achievement>();
        achievements.add(new Achievement(0, "Favorite mode", 0, 0, "Practice"));
        achievements.add(new Achievement(0, "Favorite category", 0, 0, "Computers"));
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));

        AchievementsAdapter adapter = new AchievementsAdapter(getActivity(),  achievements);
        statsList.setAdapter(adapter);
        achievementsList.setAdapter(adapter);
        return ll;
    }
}
