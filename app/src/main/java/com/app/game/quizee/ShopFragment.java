package com.app.game.quizee;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.BackEndManager;
import com.app.game.quizee.backend.GameItem;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.List;

public class ShopFragment extends Fragment {

    ListView shopListView;
    public ShopFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_shop, container, false);
        shopListView = (ListView) ll.findViewById(R.id.shop_listview);
        ShopAdapter sa = new ShopAdapter(getActivity(), BackEndManager.mes_item);
        shopListView.setAdapter(sa);

        return ll;
    }

    private class ShopAdapter extends ArrayAdapter<GameItem> {
        private Context context; //context

        ShopAdapter(Activity activityContext, List<GameItem> items){
            super(activityContext, R.layout.shop_item_list_layout, items);
            this.context = activityContext;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final Player current_player = PlayerManager.getInstance().getCurrentPlayer();
            // inflate the layout for each list row
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.shop_item_list_layout, parent, false);
            }

            final GameItem rowItem = getItem(position);
            //si le row est un power up
            TextView powUpName = (TextView) convertView.findViewById(R.id.shop_item_name);
            TextView powUpPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
            final TextView powUpCount = (TextView) convertView.findViewById(R.id.shop_item_quantity);
            ImageView powUpIcon = (ImageView) convertView.findViewById(R.id.shop_item_icon);
            ImageButton buy = (ImageButton) convertView.findViewById(R.id.shop_buy_button);
            final TextView powUpDescription = (TextView) convertView.findViewById(R.id.shop_item_description);

            //ajoute une action lorsque lon achete un power up
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_player.purchase(rowItem)) {
                        Toast.makeText(getContext(), " +1 " + rowItem.getName() + " purchased!", Toast.LENGTH_SHORT).show();
                        BackEndManager.updateAchievements(current_player, getContext());
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Not enough money to buy: " + rowItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            powUpDescription.setText(rowItem.getDescription());
            powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getNumberItemPurchased(rowItem.getClass()));
            powUpName.setText(rowItem.getType());
            powUpPrice.setText(String.valueOf(rowItem.getPrice()));
            powUpIcon.setImageResource(rowItem.getImageId());

            return convertView;
        }

    }
}
