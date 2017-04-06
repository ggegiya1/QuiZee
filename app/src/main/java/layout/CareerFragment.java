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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button careerButton;
    Button categoryButton;
    Button epicButton;
    Button statsButton;
    Button modeButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CareerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CareerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CareerFragment newInstance(String param1, String param2) {
        CareerFragment fragment = new CareerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() TODO remove?
            //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
