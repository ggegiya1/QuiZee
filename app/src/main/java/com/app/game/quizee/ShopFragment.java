package com.app.game.quizee;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.game.quizee.BackEnd.BackEndManager;
import com.app.game.quizee.BackEnd.Category;

import java.util.Set;

public class ShopFragment extends Fragment {

    public ShopFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_shop, container, false);
        LinearLayout categoriesShop = (LinearLayout) fl.findViewById(R.id.categories_linear_layout);
        LinearLayout powUpShop = (LinearLayout) fl.findViewById(R.id.power_ups_linear_layout);


        //ajoute une liste de toutes les powerUps achetables dans le magasin et laffiche
        Set<String> keys = BackEndManager.item_cost.keySet();
        for (String name : keys) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView powUpName = (TextView) relL.findViewById(R.id.shop_item_name);
            TextView powUpPrice = (TextView) relL.findViewById(R.id.shop_item_price);
            TextView powUpCount = (TextView) relL.findViewById(R.id.shop_item_quantity);
            ViewSwitcher boughtSwitch = (ViewSwitcher) relL.findViewById(R.id.shop_item_viewswitcher);
            ImageButton buy = (ImageButton) relL.findViewById(R.id.shop_buy_button);

            //montre le bouton dachat
            boughtSwitch.showNext();

            //ajoute une action lorsque lon achete un power up
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Trouver QUEL item on a cliqué (bombe, temps, etc.)
                    //TODO Esnuite caller UserProfile.buy_one("Bombe");
                    Toast.makeText(getContext(), "You bought a powerup!", Toast.LENGTH_LONG).show();
                }
            });


            //ajoute un espace entre les views
            View view = new View(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, 15

            );
            view.setLayoutParams(params);

            powUpName.setText(name);
            powUpShop.addView(relL);
            powUpShop.addView(view);
        }

        //ajoute une liste de toutes les categories achetables dans le magasin et laffiche
        for (final Category category : BackEndManager.mes_cate) {
            RelativeLayout relL = (RelativeLayout) inflater.inflate(R.layout.shop_item_list_layout, container, false);
            TextView catName = (TextView) relL.findViewById(R.id.shop_item_name);
            TextView catPrice = (TextView) relL.findViewById(R.id.shop_item_price);
            TextView catCount = (TextView) relL.findViewById(R.id.shop_item_quantity);
            ImageButton catBuyButton = (ImageButton) relL.findViewById(R.id.shop_buy_button);
            final ViewSwitcher boughtSwitch = (ViewSwitcher) relL.findViewById(R.id.shop_item_viewswitcher);
            catCount.setVisibility(View.INVISIBLE);
            String[] parts = category.get_name().split("\\s+");
            catPrice.setText(Integer.toString((int)category.get_price()));
            catName.setText(parts[parts.length-1]);
            categoriesShop.addView(relL);

            //verifie si la categorie a deja ete acheté TODO programmaticallement
            if(false) {

            }   else {
                //verifie si la categorie est achetable TODO programmaticallement
                if(true) {
                    boughtSwitch.showNext();
                    catBuyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder Ad = new AlertDialog.Builder(getActivity());
                            Ad.setIcon(android.R.drawable.ic_dialog_alert);
                            Ad.setMessage("Do you want to buy the category : " + category.get_name());
                            Ad.setTitle(R.string.AlertDialog_shop);

                            //actions when user says yes
                            Ad.setPositiveButton(R.string.yes , new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    boughtSwitch.showPrevious();
                                    //que faire lorsque lon achete la categorie TODO


                                }
                            } );
                            Ad.setNegativeButton(R.string.no, null);
                            Ad.show();
                        }
                    });
                }
            }

            View view = new View(getActivity());
            //ajoute un espace entre les views
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, 15

            );
            view.setLayoutParams(params);
            categoriesShop.addView(view);
        }
        return fl;
    }

    //TODO complete this
    private void add_powerups(){


    }





}
