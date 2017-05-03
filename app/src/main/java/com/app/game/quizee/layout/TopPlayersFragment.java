package com.app.game.quizee.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopPlayersFragment extends Fragment implements PlayerManager.TopListReceivedCallback{

    private static final String TAG = "contact.fragment";
    private static final int MAX_TOP_PLAYERS = 20;
    private ListView topList;
    private ContactAdapter contactAdapter;
    private boolean myswitch=true;
    public TopPlayersFragment() {}

    /**
     * Creates the view to show top players
     * Calculates a max amount of players to show following the variable MAX_TOP_PLAYERS
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactAdapter = new ContactAdapter(getActivity(), new ArrayList<Player>(MAX_TOP_PLAYERS));
        PlayerManager.getInstance().setTopListReceivedCallback(this);

        View rl = inflater.inflate(R.layout.fragment_top_players, container, false);
        topList = (ListView) rl.findViewById(R.id.top_players_list);
        topList.setAdapter(contactAdapter);
        final Button monbtn = (Button) rl.findViewById(R.id.highscore_btn);
        final TextView hsview = (TextView) rl.findViewById(R.id.highscoretext);
        clickswitch (myswitch,monbtn,hsview);
        monbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickswitch(myswitch,monbtn,hsview);
                if (myswitch){
                    myswitch=false;
                }else{
                    myswitch=true;
                }
            }
        });
        return rl;
    }
    private void clickswitch(boolean total, Button monbtn, TextView hs){
        if (total){
            //We want the total score
            monbtn.setText(getResources().getString(R.string.filter2));
            PlayerManager.getInstance().getTopPlayersTotal(MAX_TOP_PLAYERS);
            hs.setText(getResources().getString(R.string.filter4));
        }else{
            //We want the high score
            monbtn.setText(getResources().getString(R.string.filter1));
            PlayerManager.getInstance().getTopPlayers(MAX_TOP_PLAYERS);
            hs.setText(getResources().getString(R.string.filter3));
        }
    }
    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), "Database error: " + message, Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO: WHAT?
     * @param player
     */
    @Override
    public void onItemRead(Player player) {
        Log.i(TAG, "Contact read" + player);
        contactAdapter.remove(player);
        contactAdapter.add(player);
        contactAdapter.sort(new Comparator<Player>() {
            @Override
            public int compare(Player lhs, Player rhs) {
                if (myswitch) {
                    return rhs.getTotalratio() - lhs.getTotalratio();

                }
                return rhs.getHighestScore() - lhs.getHighestScore();
            }
        });
    }

    /**
     * Holds a view to be recycled later for better performance
     */
    private static class ViewHolder {
        TextView name;
        ImageView icon;
        TextView score;
        TextView level;
    }

    /**Adapter inspired from
    * http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
     */
    private class ContactAdapter extends ArrayAdapter<Player>{

        Activity context;
        /**
         * Contact adapter constructor
         */
        ContactAdapter (Activity context, List<Player> players) {
            super(context, R.layout.top_players_item_list_layout, new ArrayList<Player>(players));
            this.context = context;
        }

        /**
         * Returns a row to be used in contact adapter
         */
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            ViewHolder holder;

            Player p = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.top_players_item_list_layout, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.contact_item_name);
                holder.icon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon);
                holder.score = (TextView) convertView.findViewById(R.id.contact_score);
                holder.level = (TextView) convertView.findViewById(R.id.contact_level);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(p.getName());
            if (myswitch){
                holder.score.setText(String.valueOf(p.getTotalratio()));
            }else{
                holder.score.setText(String.valueOf(p.getHighestScore()));
            }
            holder.level.setText(String.valueOf(p.getLevel()));
            holder.icon.setImageBitmap(p.avatarBitmap());
            return convertView;
        }

    }
}
