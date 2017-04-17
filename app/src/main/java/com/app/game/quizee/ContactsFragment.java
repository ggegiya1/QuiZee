package com.app.game.quizee;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.app.game.quizee.backend.Player;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    ListView favoriteList;
    ListView suggestedList;
    SearchView contactSearch;
    boolean searching;
    ViewSwitcher contactViewSwitcher;
    ListView contactSearchList;

    // Required empty public constructor
    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searching = false;
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_contacts, container, false);



        contactSearch = (SearchView) ll.findViewById(R.id.contact_search);
        favoriteList = (ListView) ll.findViewById(R.id.favorite_contacts_list);
        suggestedList = (ListView) ll.findViewById(R.id.suggested_players_list);
        contactViewSwitcher = (ViewSwitcher) ll.findViewById(R.id.contact_view_switcher);
        contactSearchList = (ListView) ll.findViewById(R.id.contact_search_list);

        contactSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searching) {
                    contactViewSwitcher.showNext();
                    search(contactSearch.getQuery());
                }
                searching = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        contactSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(searching) {
                    searching = false;
                    contactViewSwitcher.showPrevious();
                }
                return false;
            }
        });

        ArrayList<Player> playerListFiller = new ArrayList<Player>();
        playerListFiller.add(new Player("Britney", "Britney", null, 10));
        playerListFiller.add(new Player("Britney", "Britney", null, 11));
        playerListFiller.add(new Player("Britney", "Britney", null, 12));
        playerListFiller.add(new Player("Britney", "Britney", null, 13));
        playerListFiller.add(new Player("Britney", "Britney", null, 14));
        playerListFiller.add(new Player("Britney", "Britney", null, 15));

        //TODO aller chercher programmaticallement images, niveaux ,et noms de contacts et les passer directement dans ladapteur

        ContactAdapter adapterFavorite = new ContactAdapter(getActivity(), playerListFiller);
        favoriteList.setAdapter(adapterFavorite);

        ContactAdapter adapterSuggested = new ContactAdapter(getActivity(), playerListFiller);
        suggestedList.setAdapter(adapterSuggested);

        return ll;
    }

    //contien temporairement un view pour la réutiliser
    private static class ViewHolder {
        TextView contactName;
        ImageView contactIcon;
        ImageView contactOnline;
        TextView levelTv;
        ImageButton addContact;
        ImageButton removeContact;
        Button playRequestButton;
        ProgressBar pb;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class ContactAdapter extends ArrayAdapter<Player> {

        Activity context;
        boolean alreadyAddedContacts;

        //TODO passer les contacts directements dans ladapteur
        public ContactAdapter (Activity context, ArrayList<Player> players) {
            super(context, R.layout.contacts_item_list_layout, players);

            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            ViewHolder holder;

            Player p = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.contacts_item_list_layout, null, true);
                holder = new ViewHolder();
                holder.contactName = (TextView) convertView.findViewById(R.id.contact_item_name);
                holder.contactIcon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon);
                holder.contactOnline = (ImageView) convertView.findViewById(R.id.contact_connected);
                holder.levelTv = (TextView) convertView.findViewById(R.id.contact_level);
                holder.addContact = (ImageButton) convertView.findViewById(R.id.contact_add_button);
                holder.removeContact = (ImageButton) convertView.findViewById(R.id.contact_remove_button);
                holder.playRequestButton = (Button) convertView.findViewById(R.id.play_request_button);
                holder.pb = (ProgressBar) convertView.findViewById(R.id.play_request_progressbar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.pb.setVisibility(View.INVISIBLE);
            holder.playRequestButton.setVisibility(Button.INVISIBLE);

            if (alreadyAddedContacts) {
                holder.addContact.setVisibility(View.VISIBLE);
                holder.removeContact.setVisibility(View.VISIBLE);
                holder.removeContact.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //TODO ajouter une action lorsque lon enleve un contact
                    }
                });
            } else {
                holder.removeContact.setVisibility(ImageButton.INVISIBLE);
                holder.addContact.setVisibility(View.VISIBLE);
                holder.addContact.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //TODO ajouter une action lorsque lon ajoute un contact
                    }
                });
            }

            if(p.isOnline()) {
                holder.contactOnline.setColorFilter(Color.GREEN);
            } else {
                holder.contactOnline.setColorFilter(Color.RED);
            }

            holder.contactName.setText(p.getName());

            if(p.getImg() != null) {
                //holder.contactIcon.setImageResource(p.getImg()); TODO arranger la facon dont on trouve les images
            }
            holder.levelTv.setText(Integer.toString((p.getLevel())));
            return convertView;
        }
    }


    //ce qui se passe lors dune recherche
    private void search (CharSequence search) {
        //TODO aller chercher les resultats de recherche automatiquement

        ArrayList<Player> playerListFiller = new ArrayList<Player>();
        playerListFiller.add(new Player("Britney", "Britney", null, 10));
        playerListFiller.add(new Player("Britney", "Britney", null, 11));
        playerListFiller.add(new Player("Britney", "Britney", null, 12));
        playerListFiller.add(new Player("Britney", "Britney", null, 13));
        playerListFiller.add(new Player("Britney", "Britney", null, 14));
        playerListFiller.add(new Player("Britney", "Britney", null, 15));

        ContactAdapter adapterSearch = new ContactAdapter(getActivity(), playerListFiller);
        contactSearchList.setAdapter(adapterSearch);
    }
}
