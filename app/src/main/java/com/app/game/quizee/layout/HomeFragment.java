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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    Button quickPlay;
    Button favoriteCategoryPlay;
    Button categoryPlay;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_home, container, false);
        quickPlay = (Button) fl.findViewById(R.id.button_quickPlay);
        favoriteCategoryPlay = (Button) fl.findViewById(R.id.button_multiPlay);
        categoryPlay = (Button) fl.findViewById(R.id.button_Play_Categories);
        quickPlay.setOnClickListener(quickPlay());
        categoryPlay.setOnClickListener(categoriesPlay());
        favoriteCategoryPlay.setOnClickListener(favoriteCategoryPlay());
        return fl;
    }

    private View.OnClickListener quickPlay(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iquick = new Intent(getContext(), QuestionActivity.class);
                iquick.putExtra("isPracticeMode", true);
                startActivity(iquick);
            }
        };
    }

    private View.OnClickListener favoriteCategoryPlay(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imulti = new Intent(getContext(), FavoriteCategorySelectionActivity.class);
                startActivity(imulti);
            }
        };
    }

    private View.OnClickListener categoriesPlay(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icat = new Intent(getContext(), CategorySelectionActivity.class);
                startActivity(icat);
            }
        };
    }
}
