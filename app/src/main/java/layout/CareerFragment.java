package layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.game.quizee.R;

public class CareerFragment extends Fragment {

    ListView statsList;
    ListView achievementsList;

    public CareerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_career, container, false);

        achievementsList = (ListView) ll.findViewById(R.id.achievements_list);
        statsList = (ListView) ll.findViewById(R.id.stats_list);

        String[] stats = new String[]{"favorite mode", "Games played", "Questions answered"};

        //TODO aller chercher les informations dachievement programmaticallement

        int[] detailsImageId = new int[]{R.drawable.ic_contacts, R.drawable.ic_multi_player, R.drawable.ic_art, R.drawable.ic_skip, R.drawable.ic_geography, R.drawable.ic_notifications_black_24dp};
        boolean[] detailsCompletion = new boolean[]{false, false, true, false};
        boolean[] nominal = new boolean[]{true, true, false, true};
        String[] detailsString = new String[]{"favorite mode", "Games played", "Questions answered", "favorite category"};
        int[] progression = new int[]{10, 60, 70, 90};
        String[] detailsNominalString = new String[]{"practice", "15", "150", "Video Games"};


        DetailsAdapter adapter = new DetailsAdapter(getActivity(), detailsString, detailsImageId, progression, nominal, detailsCompletion, detailsNominalString);
        statsList.setAdapter(adapter);
        achievementsList.setAdapter(adapter);
        return ll;
    }

    private static class ViewHolder {
        TextView achievementName;
        ImageView achievementIcon;
        ImageView check;
        TextView nominalInformation;
        ProgressBar pb;
        TextView expGiven;
        TextView goldGiven;
    }

    //Adapter inspiré de
    // http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class DetailsAdapter extends ArrayAdapter<String> {

        String[] detailsName;
        int[] detailsImageId;
        int[] progression;
        Activity context;
        boolean[] nominal;
        boolean[] completed;

        public DetailsAdapter (Activity context, String[] itemname, int[] imgid, int[] progress, boolean[] nomin, boolean[] complete, String[] nomDetails) {
            super(context, R.layout.contacts_item_list_layout, itemname);

            this.detailsName = itemname;
            this.context=context;
            this.detailsImageId=imgid;
            this.progression=progress;
            this.nominal=nomin;
            this.completed=complete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.achievements_item_list_layout, null, true);
                holder = new ViewHolder();
                holder.achievementName = (TextView) convertView.findViewById(R.id.achievement_item_name);
                holder.achievementIcon = (ImageView) convertView.findViewById(R.id.contact_avatar_icon);
                holder.expGiven = (TextView) convertView.findViewById(R.id.achievement_exp_given);
                holder.goldGiven = (TextView) convertView.findViewById(R.id.achievement_gold_given);
                holder.check = (ImageView) convertView.findViewById(R.id.achievement_check);
                holder.nominalInformation = (TextView) convertView.findViewById(R.id.achievement_information);
                holder.pb = (ProgressBar) convertView.findViewById(R.id.achievement_progress);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.achievementName.setText(detailsName[position]);

            return convertView;
        }
    }
}
