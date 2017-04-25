package com.app.game.quizee;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.app.game.quizee.backend.GameItem;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

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
        ShopAdapter sa = new ShopAdapter(getActivity(), BackEndManager.mes_item);
        shopListView.setAdapter(sa);

        return ll;
    }

    private class ShopAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<GameItem> items; //data source of the list adapter

        //public constructor
        public ShopAdapter(Context context, ArrayList<GameItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size() + 1; //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return R.string.shop_power_ups;
            } else {
                return items.get(position - 1);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Player current_player = PlayerManager.getInstance().getCurrentPlayer();
            // inflate the layout for each list row
            if (convertView == null || (convertView.getId() == R.id.list_title && position != 5 && position != 0)) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.shop_item_list_layout, parent, false);
            } else if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.title_list, parent, false);
            }


            if (position == 0) {
                //si le row est un titre
                TextView title = (TextView) LayoutInflater.from(context).
                        inflate(R.layout.title_list, parent, false);
                title.setText(R.string.shop_power_ups);
                return title;
            } else {
                final GameItem rowItem = (GameItem) getItem(position);
                //si le row est un power up
                TextView powUpName = (TextView) convertView.findViewById(R.id.shop_item_name);
                TextView powUpPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
                final TextView powUpCount = (TextView) convertView.findViewById(R.id.shop_item_quantity);
                ImageView powUpIcon = (ImageView) convertView.findViewById(R.id.shop_item_icon);
                ViewSwitcher boughtSwitch = (ViewSwitcher) convertView.findViewById(R.id.shop_item_viewswitcher);
                ImageButton buy = (ImageButton) convertView.findViewById(R.id.shop_buy_button);

                //montre le bouton dachat
                boughtSwitch.setInAnimation(null);
                boughtSwitch.setDisplayedChild(1);

                powUpCount.setVisibility(View.VISIBLE);
                udpate_powerups(current_player, position,powUpCount);

                //ajoute une action lorsque lon achete un power up
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_player.purchase(rowItem)) {
                            Toast.makeText(getContext(), " +1 " + rowItem.getName() + " purchased!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough money to buy: " + rowItem.getName(), Toast.LENGTH_LONG).show();
                        }
                        udpate_powerups(current_player, rowItem.getPosition(),powUpCount);

                    }
                });
                powUpName.setText(rowItem.getType());
                powUpPrice.setText(Integer.toString(rowItem.getPrice()));
                powUpIcon.setImageResource(rowItem.getImageId());

            }
            return convertView;
        }

        private void udpate_powerups(Player current_player, int pos, TextView powUpCount) {
            switch (pos) {
                //TODO: Manque affichage achievement
                case 1:
                    powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getBombs());
                    if (current_player.getitembought("Bomb") == 5 && !current_player.checkachie(8)){
                        current_player.setachie(8);
                    }
                case 2:
                    powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getSkips());
                    if (current_player.getitembought("Skip") == 5 && !current_player.checkachie(9)){
                        current_player.setachie(9);
                    }
                case 3:
                    powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getAddTimes());
                    if (current_player.getitembought("Time") == 5 && !current_player.checkachie(10)){
                        current_player.setachie(10);
                    }
                case 4:
                    powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getHints());
                    if (current_player.getitembought("Hint") == 5 && !current_player.checkachie(11)){
                        current_player.setachie(11);
                    }
            }
        }
    }

}
