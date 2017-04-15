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
import android.widget.Toast;

import java.util.Set;
import com.app.game.quizee.BackEnd.BackEndManager;
import com.app.game.quizee.BackEnd.Category;
import com.app.game.quizee.Bottom_Navigation;
import com.app.game.quizee.R;

public class ShopFragment extends Fragment {

    public ShopFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ScrollView linl = (ScrollView) inflater.inflate(R.layout.fragment_shop, container, false);
        LinearLayout categoriesShop = (LinearLayout) linl.findViewById(R.id.categories_linear_layout);
        LinearLayout powerUpsShop = (LinearLayout) linl.findViewById(R.id.power_ups_linear_layout);

        //ajoute une liste de toutes les powerUps achetables dans le magasin
        Set<String> keys = BackEndManager.item_cost.keySet();
        for (String name : keys) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView powUpName = (TextView) relL.findViewById(R.id.shop_item);
            ImageButton buy = (ImageButton) relL.findViewById(R.id.shop_buy_button);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Trouver QUEL item on a cliqué (bombe, temps, etc.)
                    //TODO Esnuite caller UserProfile.buy_one("Bombe");
                    Toast.makeText(getContext(), "You bought a powerup!", Toast.LENGTH_LONG).show();
                }
            });


            powUpName.setText(name);
            powerUpsShop.addView(relL);
        }

        //ajoute une liste de toutes les categories achetables dans le magasin
        for (Category category : BackEndManager.mes_cate) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView catName = (TextView) relL.findViewById(R.id.shop_item);
            String[] parts = category.get_name().split("\\s+");
            catName.setText(parts[parts.length-1]);
            categoriesShop.addView(relL);
        }
        return linl;
    }

    private void add_powerups(){


    }


}
