package com.app.game.quizee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShopFragment extends Fragment {

    public ShopFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ScrollView linl = (ScrollView) inflater.inflate(R.layout.fragment_shop, container, false);

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
}
