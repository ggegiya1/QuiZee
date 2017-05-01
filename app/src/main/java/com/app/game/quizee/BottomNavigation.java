package com.app.game.quizee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.game.quizee.backend.MusicService;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.layout.CareerFragment;
import com.app.game.quizee.layout.HomeFragment;

import java.util.Observable;
import java.util.Observer;


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

    //for music service
    private boolean mIsBound = false;
    protected MusicService mServ;

    @Override
    protected void onPause() {
        //MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        player.addObserver(this);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);

        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        playerName = (TextView) findViewById(R.id.user_name_main);
        points = (TextView) findViewById(R.id.points_main);
        level = (TextView) findViewById(R.id.level_main);
        avatarView = (ImageView) findViewById(R.id.avatar_main);

        updateUserInfo();
        startMusic();

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

    private void startMusic() {
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);
    }

    private void updateUserInfo() {
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
        avatar = PlayerManager.getInstance().getCurrentPlayer().avatarBitmap();
        if(avatar != null) {
            avatarView.setImageBitmap(avatar);
        }
    }

    //on settings button clicked
    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
        intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onRestart();

    }

    @Override
    protected void onResume() {
        updateUserInfo();
        super.onResume();
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
        PlayerManager.getInstance().saveCurrentPlayer();
        MusicService.getInstance().pauseMusic();
        super.onStop();
        PlayerManager.getInstance().onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MusicService.class));
        super.onDestroy();
    }

    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = MusicService.ServiceBinder.getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };


    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateUserInfo();
    }
}
