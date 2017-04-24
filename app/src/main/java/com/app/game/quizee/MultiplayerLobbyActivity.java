package com.app.game.quizee;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MultiplayerLobbyActivity extends AppCompatActivity {

    final long requestLength = 10000; //Les requetes pours jouer durent 10 secondes
    ListView multiplayerList;
    int notif_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);

        multiplayerList = (ListView) findViewById(R.id.contact_multiplayer_list);

        Integer[] favoriteImageId = new Integer[] {R.drawable.ic_notifications_black_24dp, R.drawable.ic_skip, R.drawable.ic_geography};
        Integer[] favoriteLevels = new Integer[] {5, 3, 21};
        String[] favorite = new String[] {"Stephen", "Bob", "Jimmy"};

        ContactAdapter adapterFavorite = new ContactAdapter(this, favorite, favoriteImageId, favoriteLevels);
        multiplayerList.setAdapter(adapterFavorite);


    }

    //on play button pressed TODO remove later
    public void multiPlay(View v) {
        Intent intent = new Intent(getApplicationContext(), MultiplayerQuestionActivity.class);
        //Fetch les 2 id des joueurs et envoie à multiplayer question activity
        intent.putExtra("ID_P1", 0);
        intent.putExtra("ID_P2", 0);
        startActivity(intent);
    }

    public void simpleNotification(){
        NotificationCompat.Builder notifBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_name)
                        .setContentTitle("Want to play?")
                        .setContentText("A friend has invited you to play!")
                        .setAutoCancel(true)
                        .setPriority(2);

        Intent resultIntent = new Intent(this, MultiplayerQuestionActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BottomNavigation.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notifBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notif_id,notifBuilder.build());
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class ContactAdapter extends ArrayAdapter<String> {

        String[] itemname;
        Integer[] imgid;
        Integer[] level;
        Activity context;

        public ContactAdapter (Activity context, String[] itemname, Integer[] imgid, Integer[] lvl) {
            super(context, R.layout.contacts_item_list_layout, itemname);

            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
            this.level=lvl;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.contacts_item_list_layout, null,true);

            TextView contactName = (TextView) rowView.findViewById(R.id.contact_item_name);
            ImageView contactIcon = (ImageView) rowView.findViewById(R.id.contact_avatar_icon);
            TextView levelTv = (TextView) rowView.findViewById(R.id.contact_level);

            Button addContact = (Button) rowView.findViewById(R.id.contact_follow_toggle);
//            ImageButton removeContact = (ImageButton) rowView.findViewById(R.id.contact_remove_button);
//            final Button playRequestButton = (Button) rowView.findViewById(R.id.play_request_button);
//            final ProgressBar pb = (ProgressBar) rowView.findViewById(R.id.play_request_progressbar);
            ImageView contactConnectionCircle = (ImageView) rowView.findViewById(R.id.contact_is_connected);

            contactConnectionCircle.setVisibility(View.VISIBLE);
            contactConnectionCircle.setColorFilter(0x00ff0000, PorterDuff.Mode.MULTIPLY);
            addContact.setVisibility(View.INVISIBLE);
//            removeContact.setVisibility(View.INVISIBLE);
//
//            playRequestButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    v.setVisibility(View.INVISIBLE);
//                    pb.setVisibility(View.VISIBLE);
//                    CountDownTimer cdt = new CountDownTimer(requestLength, 100) {
//                        @Override
//                        public void onTick(long millisUntilFinished) {
//
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            pb.setVisibility(View.INVISIBLE);
//                            playRequestButton.setVisibility(View.VISIBLE);
//                            //TODO prevenir lautre joueur que la requete pour jouer est finie
//                        }
//                    }.start();
//                    //TODO que faire lorsque lon demande a un joueur de jouer
//                    simpleNotification();
//                }
//            });

            contactName.setText(itemname[position]);
            contactIcon.setImageResource(imgid[position]);
            levelTv.setText(Integer.toString(level[position]));
            return rowView;
        }
    }
}
