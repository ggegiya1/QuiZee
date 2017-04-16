package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.game.quizee.CategorySelectionActivity;
import com.app.game.quizee.MultiplayerLobbyActivity;
import com.app.game.quizee.QuestionActivity;
import com.app.game.quizee.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    Button quickPlay;
    Button multiPlay;
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
        multiPlay = (Button) fl.findViewById(R.id.button_multiPlay);
        categoryPlay = (Button) fl.findViewById(R.id.button_Play_Categories);
        quickPlay.setOnClickListener(quickPlay());
        categoryPlay.setOnClickListener(categoriesPlay());
        multiPlay.setOnClickListener(multiPlay());
        return fl;
    }

    private View.OnClickListener quickPlay(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iquick = new Intent(getContext(), QuestionActivity.class);
                startActivity(iquick);
            }
        };
    }

    private View.OnClickListener multiPlay(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imulti = new Intent(getContext(), MultiplayerLobbyActivity.class);
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
