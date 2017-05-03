package com.app.game.quizee.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopPlayersFragment extends Fragment implements PlayerManager.TopListReceivedCallback, AdapterView.OnItemSelectedListener{

    private static final String TAG = "contact.fragment";
    private static final int MAX_TOP_PLAYERS = 20;
    private ContactAdapter contactAdapter;
    private boolean isHighScoreSort = true;

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
        ListView topList = (ListView) rl.findViewById(R.id.top_players_list);
        topList.setAdapter(contactAdapter);
        createSpinner(rl);
        return rl;
    }

    private void createSpinner(View view){
        Spinner spinner = (Spinner) view.findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sort_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String selected = (String)parent.getItemAtPosition(pos);
        if ("High Score".equals(selected)){
            sortByHighScore();
        }else {
            sortByTotalScore();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void sortByHighScore(){
        isHighScoreSort = true;
        PlayerManager.getInstance().getTopPlayers(MAX_TOP_PLAYERS);
    }


    private void sortByTotalScore(){
        isHighScoreSort = false;
        PlayerManager.getInstance().getTopPlayersTotal(MAX_TOP_PLAYERS);
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
                if (isHighScoreSort) {
                    return rhs.getHighestScore() - lhs.getHighestScore();
                }
                return rhs.getTotalratio() - lhs.getTotalratio();
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
            if (isHighScoreSort){
                holder.score.setText(String.valueOf(p.getHighestScore()));
            }else{
                holder.score.setText(String.valueOf(p.getTotalratio()));
            }
            holder.level.setText(String.valueOf(p.getLevel()));
            holder.icon.setImageBitmap(p.avatarBitmap());
            return convertView;
        }

    }
}
