package com.app.game.quizee;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.game.quizee.backend.BackEndManager;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Item;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    ListView shopListView;

    public ShopFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_shop, container, false);
        shopListView = (ListView) ll.findViewById(R.id.shop_listview);
        List<Category> categories = CategoryManager.getInstance().getAllCategoriesSortedByPrice();
        ShopAdapter sa = new ShopAdapter(getActivity(), BackEndManager.mes_item, categories);
        shopListView.setAdapter(sa);


        return ll;
    }

    private class ShopAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<Item> items; //data source of the list adapter
        private List<Category> categories;

        //public constructor
        public ShopAdapter(Context context, ArrayList<Item> items, List<Category> categories) {
            this.context = context;
            this.items = items;
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return items.size() + categories.size() + 2; //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            if(position == 0) {
                return R.string.shop_power_ups;
            } else if(position == 5) {
                return R.string.shop_Caterogies;
            } else if(position < 5) {
                return items.get(position - 1);
            } else {
                return categories.get(position - 2 - items.size());
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // inflate the layout for each list row
            if (convertView == null || (convertView.getId() == R.id.list_title && position != 5 && position != 0))
            {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.shop_item_list_layout, parent, false);
            } else if (convertView == null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.title_list, parent, false);
            }



            if(position == 0 || position == 5) {

                //si le row est un titre
                TextView title = (TextView) LayoutInflater.from(context).
                        inflate(R.layout.title_list, parent, false);
                if(position == 0) {title.setText(R.string.shop_power_ups);
                } else {
                    //convertView.setText(R.string.shop_power_ups);
                };
                return title;
            } else if (position > 0 && position < 5) {
                Item rowItem = (Item) getItem(position);
                //si le row est un power up
                TextView powUpName = (TextView) convertView.findViewById(R.id.shop_item_name);
                TextView powUpPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
                TextView powUpCount = (TextView) convertView.findViewById(R.id.shop_item_quantity);
                ImageView powUpIcon = (ImageView) convertView.findViewById(R.id.shop_item_icon);
                ViewSwitcher boughtSwitch = (ViewSwitcher) convertView.findViewById(R.id.shop_item_viewswitcher);
                ImageButton buy = (ImageButton) convertView.findViewById(R.id.shop_buy_button);

                //montre le bouton dachat
                boughtSwitch.setInAnimation(null);
                boughtSwitch.setDisplayedChild(1);

                powUpCount.setVisibility(View.VISIBLE);

                //TODO get item count programmatically
                powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + 3);


                //ajoute une action lorsque lon achete un power up
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Trouver QUEL item on a cliqué (bombe, temps, etc.)
                        //TODO Esnuite caller UserProfile.buy_one("Bombe");
                        Toast.makeText(getContext(), "You bought a powerup!", Toast.LENGTH_LONG).show();
                    }
                });
                powUpName.setText(rowItem.getType());
                powUpPrice.setText(Integer.toString(rowItem.getCost()));
                powUpIcon.setImageResource(rowItem.getImageId());

            } else if (position >5) {
                final Category category = (Category) getItem(position);

                TextView catName = (TextView) convertView.findViewById(R.id.shop_item_name);
                TextView catPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
                TextView catCount = (TextView) convertView.findViewById(R.id.shop_item_quantity);
                ImageView catIcon = (ImageView) convertView.findViewById(R.id.shop_item_icon);
                ImageButton catBuyButton = (ImageButton) convertView.findViewById(R.id.shop_buy_button);
                final ViewSwitcher boughtSwitch = (ViewSwitcher) convertView.findViewById(R.id.shop_item_viewswitcher);
                catCount.setVisibility(View.INVISIBLE);
                String[] parts = category.getName().split("\\s+");
                catPrice.setText(Integer.toString((int)category.getPrice()));
                catName.setText(parts[parts.length-1]);
                catIcon.setImageResource(category.getImageId());

                boughtSwitch.setInAnimation(null);

                //verifie si la categorie a deja ete acheté TODO programmaticallement
                if(false) {

                }   else {



                    //verifie si la categorie est achetable TODO programmaticallement
                    if(true) {
                        boughtSwitch.setDisplayedChild(1);
                        catBuyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder Ad = new AlertDialog.Builder(getActivity());
                                Ad.setIcon(getResources().getDrawable(category.getImageId()));
                                Ad.setMessage("Do you want to buy the category : " + category.getName() + " for " + ((int) category.getPrice()) + " Quizee Dollars?");
                                Ad.setTitle("Buy " + category.getName());

                                //actions when user buys a category
                                Ad.setPositiveButton(R.string.buy , new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        boughtSwitch.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
                                        boughtSwitch.setDisplayedChild(0);
                                        //que faire lorsque lon achete la categorie TODO
                                        Toast.makeText(getActivity(), "You bought " + category.getName() + ", have fun!", Toast.LENGTH_SHORT).show();

                                    }
                                } );
                                Ad.setNegativeButton(R.string.dontbuy, null);
                                Ad.show();
                            }
                        });
                    } else {
                        //la categorie nest pas achetable TODO
                    }
                }




            }
            return convertView;
        }
    }



    //TODO completer la methode
    private void add_powerups(){

    }
}
