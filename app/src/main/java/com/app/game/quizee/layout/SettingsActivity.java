package com.app.game.quizee.layout;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.MusicService;
import com.app.game.quizee.backend.PlayerManager;

import java.io.ByteArrayOutputStream;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            setupListeners();
        }

        private void setupListeners() {
            findPreference("change_avatar").setOnPreferenceClickListener(preferenceClicked());
            findPreference("player_name").setOnPreferenceChangeListener(preferenceChanged());
            findPreference("sound_music").setOnPreferenceChangeListener(preferenceChanged());
            findPreference("logout").setOnPreferenceClickListener(preferenceClicked());
        }

        public Preference.OnPreferenceClickListener preferenceClicked () {
            return new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    switch (preference.getKey()) {
                        case "logout" :
                            PlayerManager.getInstance().logout();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            return false;
                        case "change_avatar":
                                pickImage();
                            return false;
                    }
                    return true;
                }
            };
        }

        public Preference.OnPreferenceChangeListener preferenceChanged () {

            return new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    switch (preference.getKey()) {
                        case "sound_music":
                            Boolean musicBoolean = (Boolean) o;
                            MusicService mServ = MusicService.getInstance();
                            if(musicBoolean) {
                                mServ.start();
                            } else {
                                mServ.inconditionalPauseMusic();
                            }
                            return true;
                        case "player_name":
                            String newName = (String) o;
                            PlayerManager.getInstance().getCurrentPlayer().setName(newName);
                            PlayerManager.getInstance().saveCurrentPlayer();
                            return false;
                    }
                    return true;
                }
            };
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            Log.i("settings", item.toString());
            return super.onOptionsItemSelected(item);
        }
        //choisir une image: le code vient de http://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
        public void pickImage() {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("scale", false);
            intent.putExtra("outputX", 256);
            intent.putExtra("outputY", 256);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (requestCode == 1) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    //Get image
                    Bitmap newAvatar = extras.getParcelable("data");
                    //la conversion du bitmap en string vient de
                    // http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
                    if (newAvatar!=null){
                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        newAvatar.compress(Bitmap.CompressFormat.PNG,100, baos);
                        byte [] b=baos.toByteArray();
                        String temp= Base64.encodeToString(b, Base64.DEFAULT);
                        PlayerManager.getInstance().getCurrentPlayer().setAvatar(temp);
                        PlayerManager.getInstance().saveCurrentPlayer();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.pref_account_no_image_selected), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }

    @Override
    protected void onResume() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onResume();
    }
}