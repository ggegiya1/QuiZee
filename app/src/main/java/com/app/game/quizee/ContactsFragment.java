package com.app.game.quizee;

import android.app.Activity;
import android.graphics.Matrix;
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

        //TODO aller chercher programmaticallement images, niveaux ,et noms de contacts et les passer directement dans ladapteur
        int[] favoriteImageId = new int[] {R.drawable.ic_notifications_black_24dp, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_notifications_black_24dp};
        int[] favoriteLevels = new int[] {5, 3, 21, 11 , 76, 99};
        String[] favorite = new String[] {"Stephen", "Bob", "Jimmy", "Britney", "Hug", "Max"};

        int[] suggestedImageId = new int[] {R.drawable.ic_contacts, R.drawable.ic_multi_player, R.drawable.ic_art, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_notifications_black_24dp};
        int[] suggestedLevels = new int[] {10, 1, 14, 87, 76, 4, 31};
        String[] suggested = new String[] {"Britney", "Hug", "Max","Stephen", "Bob", "Jimmy"};

        ContactAdapter adapterFavorite = new ContactAdapter(getActivity(), favorite, favoriteImageId, favoriteLevels, true);
        favoriteList.setAdapter(adapterFavorite);

        ContactAdapter adapterSuggested = new ContactAdapter(getActivity(), suggested, suggestedImageId, suggestedLevels, false);
        suggestedList.setAdapter(adapterSuggested);

        return ll;
    }

    //contien temporairement un view pour la réutiliser
    private static class ViewHolder {
        TextView contactName;
        ImageView contactIcon;
        TextView levelTv;
        ImageButton addContact;
        ImageButton removeContact;
        Button playRequestButton;
        ProgressBar pb;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class ContactAdapter extends ArrayAdapter<String> {

        String[] itemname;
        int[] imgid;
        int[] level;
        Activity context;
        boolean alreadyAddedContacts;

        //TODO passer les contacts directements dans ladapteur
        public ContactAdapter (Activity context, String[] itemname, int[] imgid, int[] lvl, boolean alreadyAddedContacts) {
            super(context, R.layout.contacts_item_list_layout, itemname);

            this.alreadyAddedContacts = alreadyAddedContacts;
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
            this.level=lvl;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.contacts_item_list_layout, null, true);
                holder = new ViewHolder();
                holder.contactName = (TextView) convertView.findViewById(R.id.contact_item_name);
                holder.contactIcon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon);
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
                Matrix matrix = new Matrix();
                holder.addContact.setScaleType(ImageView.ScaleType.MATRIX);   //required
                matrix.postRotate((float) 45, 0, 0);
                holder.addContact.setImageMatrix(matrix);
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

            holder.contactName.setText(itemname[position]);
            holder.contactIcon.setImageResource(imgid[position]);
            holder.levelTv.setText(Integer.toString(level[position]));
            return convertView;
        }
    }


    //ce qui se passe lors dune recherche
    private void search (CharSequence search) {
        //TODO aller chercher les resultats de recherche automatiquement
        int[] favoriteImageId = new int[] {R.drawable.ic_notifications_black_24dp, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_notifications_black_24dp};
        int[] favoriteLevels = new int[] {5, 3, 21, 11 , 76, 99};
        String[] favorite = new String[] {"Stephen", "Bob", "Jimmy", "Britney", "Hug", "Max"};

        ContactAdapter adapterSearch = new ContactAdapter(getActivity(), favorite, favoriteImageId, favoriteLevels, false);
        contactSearchList.setAdapter(adapterSearch);
    }

}
