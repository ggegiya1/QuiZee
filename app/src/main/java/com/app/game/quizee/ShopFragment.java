package com.app.game.quizee;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView powerUpsList;
    ListView categoriesList;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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


        ScrollView linl = (ScrollView) inflater.inflate(R.layout.fragment_shop, container, false);

        //powerUpsList = (ListView) linl.findViewById(R.id.power_ups_list); TODO inutile?
        //categoriesList = (ListView) linl.findViewById(R.id.shop_categories);

        //TODO get categories programmatically and power ups
        final String[] categories = new String[] { "Computers", "History",
                "Music", "Video Games", "Geography", "Art",};


        final String[] powerUps = new String[] { "Bomb", "Hint", "+5 seconds", "Skip question"};

        LinearLayout categoriesShop = (LinearLayout) linl.findViewById(R.id.categories_linear_layout);
        LinearLayout powerUpsShop = (LinearLayout) linl.findViewById(R.id.power_ups_linear_layout);

        //ajoute une liste de toutes les powerUps achetables dans le magasin
        for (String powerUp : powerUps) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView powUpName = (TextView) relL.findViewById(R.id.shop_item);
            ImageButton buy = (ImageButton) relL.findViewById(R.id.shop_buy_button);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO que faire lorsque lon vend un power up
                }
            });
            powUpName.setText(powerUp);
            powerUpsShop.addView(relL);
        }

        //ajoute une liste de toutes les categories achetables dans le magasin
        for (String category : categories) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView catName = (TextView) relL.findViewById(R.id.shop_item);
            catName.setText(category);
            categoriesShop.addView(relL);
        }

        //TODO get powerups and categories programmatically
        return linl;
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
            // throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
            // TODO trouver pourquoi la ligne precedante existe par default
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
