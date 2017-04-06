package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.app.game.quizee.CareerDetails;
import com.app.game.quizee.R;
import com.app.game.quizee.UserProfile;

public class CareerFragment extends Fragment {

    Button careerButton;
    Button categoryButton;
    Button epicButton;
    Button statsButton;
    Button modeButton;

    public CareerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ScrollView sv = (ScrollView) inflater.inflate(R.layout.fragment_career, container, false);

        //obtaint buttons
        careerButton = (Button) sv.findViewById(R.id.career_achievements_button);
        epicButton = (Button) sv.findViewById(R.id.epic_achievements_button);
        statsButton = (Button) sv.findViewById(R.id.stats_button);
        modeButton = (Button) sv.findViewById(R.id.mode_achievements_button);
        categoryButton = (Button) sv.findViewById(R.id.category_achievements_button);

        //set on click listeners
        careerButton.setOnClickListener(careerDetails());
        epicButton.setOnClickListener(careerDetails());
        statsButton.setOnClickListener(careerDetails());
        modeButton.setOnClickListener(careerDetails());
        categoryButton.setOnClickListener(careerDetails());

        return sv;
    }

    //define onclick actions (go to career details)
    private View.OnClickListener careerDetails(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button buttonPressed = (Button) v;
                Intent i = new Intent(getContext(), CareerDetails.class);
                i.putExtra("career details", buttonPressed.getText());
                startActivity(i);
            }
        };
    }
}
