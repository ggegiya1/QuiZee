package com.app.game.quizee.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.game.quizee.R;

public class HomeFragment extends Fragment {

    Button quickPlay;
    Button favoriteCategoryPlay;
    Button categoryPlay;

    /**
     * Required empty puclic constructor
     */

    public HomeFragment() {
    }

    /**
     * obtain view and set on click listeners
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_home, container, false);
        quickPlay = (Button) fl.findViewById(R.id.button_quickPlay);
        favoriteCategoryPlay = (Button) fl.findViewById(R.id.button_choseFavorites);
        categoryPlay = (Button) fl.findViewById(R.id.button_Play_Categories);
        quickPlay.setOnClickListener(buttonPressed());
        categoryPlay.setOnClickListener(buttonPressed());
        favoriteCategoryPlay.setOnClickListener(buttonPressed());
        return fl;
    }

    /**
     * defines onclick actions
     * @return
     */

    private View.OnClickListener buttonPressed(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_quickPlay:
                        Intent iquick = new Intent(getContext(), QuestionActivity.class);
                        iquick.putExtra("isPracticeMode", true);
                        startActivity(iquick);
                        break;
                    case R.id.button_Play_Categories:
                        Intent icat = new Intent(getContext(), CategorySelectionActivity.class);
                        startActivity(icat);
                        break;
                    case R.id.button_choseFavorites:
                        Intent chooseFavorites = new Intent(getContext(), FavoriteCategorySelectionActivity.class);
                        startActivity(chooseFavorites);
                        break;
                }
            }
        };
    }
}
