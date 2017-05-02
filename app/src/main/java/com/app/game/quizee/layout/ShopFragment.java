package com.app.game.quizee.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.backend.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ShopFragment extends Fragment implements Observer {

    ListView shopListView;
    ShopAdapter sa;

    public ShopFragment() {    }

    /**
     * Creates the shop fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_shop, container, false);
        shopListView = (ListView) ll.findViewById(R.id.shop_listview);
        sa = new ShopAdapter(getActivity());
        shopListView.setAdapter(sa);
        PlayerManager.getInstance().getCurrentPlayer().addObserver(this);
        return ll;
    }

    /**
     * updates the PowerUps ListView on fragment resume
     */

    @Override
    public void onResume() {
        super.onResume();
        sa.notifyDataSetChanged();
    }

    /**
     * Defines an adapter for the powerUps ListView
     */

    private class ShopAdapter extends ArrayAdapter<PowerUp> {
        private Context context; //context

        ShopAdapter(Activity activityContext){
            super(activityContext, R.layout.shop_item_list_layout, PowerUp.values());
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

            final PowerUp rowItem = getItem(position);
            //si le row est un power up
            TextView powUpName = (TextView) convertView.findViewById(R.id.shop_item_name);
            TextView powUpPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
            final TextView powUpCount = (TextView) convertView.findViewById(R.id.shop_item_quantity);
            ImageView powUpIcon = (ImageView) convertView.findViewById(R.id.shop_item_icon);
            ImageButton buy = (ImageButton) convertView.findViewById(R.id.shop_buy_button);
            final TextView powUpDescription = (TextView) convertView.findViewById(R.id.shop_item_description);

            if(current_player.canPurchase(rowItem)) {
                powUpPrice.setTextColor(getResources().getColor(R.color.black));
                buy.setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyDialog(rowItem);
                    }});
            } else  {
                powUpPrice.setTextColor(Color.RED);
                buy.setBackground(getResources().getDrawable(R.drawable.button_tertiary_darkest));
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Not enough money to buy: " + rowItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //ajoute une action lorsque lon achete un power up

            powUpDescription.setText(rowItem.getDescription());
            powUpCount.setText(getText(R.string.shop_you_own).toString() + " " + current_player.getNumberAvailablePowerUps(rowItem));
            powUpName.setText(rowItem.getName());
            powUpPrice.setText(String.valueOf(rowItem.getPrice()));
            powUpIcon.setImageResource(rowItem.getImageId());

            return convertView;
        }

    }

    /**
     * Builds a list of achievement
     * @param player
     * @return
     */

    public List<Achievement> updateAchievements(Player player){
        List<Achievement> achievements = new ArrayList<>();
        for (Achievement a: Achievement.values()){
            if (a.isAchieved(player)){
                player.addAchievement(a);
                achievements.add(a);
                player.addexp(a.getXP());
                Toast.makeText(getContext(), "Achievement Unlocked :" + a.getDesc(), Toast.LENGTH_LONG).show();
            }
        }
        return achievements;

    }

    /**
     * updates an upject in the powerUps listview
     * @param o
     * @param arg
     */

    @Override
    public void update(Observable o, Object arg) {
        ShopAdapter adapter = (ShopAdapter)shopListView.getAdapter();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * builds the buy dialog and shows it
     * @param pUp
     */

    private void buyDialog(final PowerUp pUp) {
        final Player player = PlayerManager.getInstance().getCurrentPlayer();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View dialogView = getLayoutInflater(getArguments()).inflate(R.layout.shop_dialog,null);
        builder.setView(dialogView);
        final AlertDialog buyDialog = builder.create();
        final NumberPicker shopDialopNumberPicker = (NumberPicker) dialogView.findViewById(R.id.shop_dialog_number_picker);
        final TextView shopDialogTitle = (TextView) dialogView.findViewById(R.id.shop_dialog_title);
        shopDialogTitle.setText(getString(R.string.buy_item) + " " + pUp.getName() + " " + getString(R.string.buy_item2));
        shopDialopNumberPicker.setMinValue(0);
        shopDialopNumberPicker.setMaxValue(player.getPoints() / pUp.getPrice());

        Button buy = (Button) dialogView.findViewById(R.id.shop_dialog_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = ((NumberPicker) buyDialog.findViewById(R.id.shop_dialog_number_picker)).getValue();
                for(int i = 0; i <= quantity; i++) {
                    player.purchase(pUp);
                }
                Toast.makeText(getContext(), " +" +quantity + " " + pUp.getName() + " purchased!", Toast.LENGTH_SHORT).show();
                updateAchievements(player);
                sa.notifyDataSetChanged();
                buyDialog.cancel();
            }
        });

        Button dontBuy = (Button) dialogView.findViewById(R.id.shop_dialog_dont_buy);
        dontBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyDialog.cancel();
            }
        });
        buyDialog.show();
        buyDialog.setCancelable(true);
    }
}
