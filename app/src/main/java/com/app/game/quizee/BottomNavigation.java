package com.app.game.quizee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.layout.CareerFragment;
import com.app.game.quizee.layout.HomeFragment;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;


public class BottomNavigation extends AppCompatActivity implements Observer {
    //inspiré de https://github.com/jaisonfdo/BottomNavigation
    // pour le view pager et le bottom_navigation
    public static final int DIALOG_FRAGMENT = 1;

    private ViewPager viewPager;
    Fragment homeFragment;
    Fragment contactsFragment;
    Fragment shopFragment;
    Fragment careerFragment;
    MenuItem prevMenuItem;
    TextView playerName;
    TextView points;
    TextView level;
    ImageView avatarView;
    Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        player.addObserver(this);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        playerName = (TextView) findViewById(R.id.user_name_main);
        points = (TextView) findViewById(R.id.points_main);
        level = (TextView) findViewById(R.id.level_main);
        avatarView = (ImageView) findViewById(R.id.avatar_main);

        updateUserInfo();
        setupAvatar();
        play_bg();
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem playMenuItem) {
                        switch (playMenuItem.getItemId()) {
                            case R.id.navigation_shop:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_contacts:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_career:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        updateUserInfo();
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
                updateUserInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        shopFragment = new ShopFragment();
        homeFragment = new HomeFragment();
        contactsFragment = new TopPlayersFragment();
        careerFragment = new CareerFragment();

        adapter.addFragment(shopFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(contactsFragment);
        adapter.addFragment(careerFragment);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); //fait partir sur le home
    }

    private void setupAvatar() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        avatar = PlayerManager.getInstance().getCurrentPlayer().getAvatarBitmap(sp);
        if(avatar != null) {
            avatarView.setImageBitmap(avatar);
        }
    }

    private void play_bg(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        if(prefs.getBoolean("sound_music", false)) {
            MediaPlayer mp;
            Random rand = new Random();
            int value = rand.nextInt(4) + 1;
            switch (value) {
                case 1:
                    mp = MediaPlayer.create(this, R.raw.bg1);
                    break;
                case 2:
                    mp = MediaPlayer.create(this, R.raw.bg2);
                    break;
                case 3:
                    mp = MediaPlayer.create(this, R.raw.bg3);
                    break;
                default:
                    mp = MediaPlayer.create(this, R.raw.bg4);
                    break;
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play_bg();
                }
            });
        }
    }



    private void updateUserInfo() {
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
    }

    //on settings button clicked
    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        PlayerManager.getInstance().saveCurrentPlayer();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
        setupAvatar();
        play_bg();
    }

    @Override
    public void onBackPressed() {
        if (!PlayerManager.getInstance().isLoggedIn()){
            super.onBackPressed();
        }
        // exit on back pressed
        PlayerManager.getInstance().onStop();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserInfo();
    }

    @Override
    protected void onStop(){
        super.onStop();
        PlayerManager.getInstance().onStop();
    }

    @Override
    public void update(Observable o, Object arg) {
        updateUserInfo();
    }
}
