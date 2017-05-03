package com.app.game.quizee.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.app.game.quizee.R;
import com.app.game.quizee.backend.MusicService;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;

import java.util.Observable;
import java.util.Observer;

/**
 * inspired from https://github.com/jaisonfdo/BottomNavigation
 * for the view_pager and bottom navigation
 */

public class BottomNavigation extends AppCompatActivity implements Observer {
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
    ViewPagerAdapter adapter;

    /**
     * Creates the view, calls methods to set on clicks listener and setup bottom navigation
     */
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

        findViews();
        updateUserInfo();
        startMusic();
        navigation.setOnNavigationItemSelectedListener(navigationItemSelected());
        viewPager.addOnPageChangeListener(pageChanged());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupFragments();
    }

    /**
     * Setups actions when navigation item is selected*
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelected() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
        };
    }

    /**
     * Finds the views for later use
     */
    private void findViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        playerName = (TextView) findViewById(R.id.user_name_main);
        points = (TextView) findViewById(R.id.points_main);
        level = (TextView) findViewById(R.id.level_main);
        avatarView = (ImageView) findViewById(R.id.avatar_main);
    }

    /**
     * Setups Fragments for the adapter
     */
    private void setupFragments() {
        shopFragment = new ShopFragment();
        homeFragment = new HomeFragment();
        contactsFragment = new TopPlayersFragment();
        careerFragment = new AchievementsFragment();

        adapter.addFragment(shopFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(contactsFragment);
        adapter.addFragment(careerFragment);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); //make it start on home
    }

    /**
     * Setups actions on page changes
     */
    private ViewPager.OnPageChangeListener pageChanged() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
                updateUserInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }

    /**
     * Starts music in services
     */
    private void startMusic() {
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
    }

    /**
     * Updates the user info in the toolbar
     */
    private void updateUserInfo() {
        Player player = PlayerManager.getInstance().getCurrentPlayer();
        playerName.setText(player.getName());
        points.setText(String.valueOf(player.getPoints()));
        level.setText(String.valueOf(player.getLevel()));
        avatar = PlayerManager.getInstance().getCurrentPlayer().avatarBitmap();
        if (avatar != null) {
            avatarView.setImageBitmap(avatar);
        }
    }

    /**
     * Setups actions when settings Button is clicked
     */
    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
        startActivity(intent);
    }

    /**
     * Restarts music when the activity restarts
     */
    @Override
    public void onRestart() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onRestart();

    }

    /**
     * Updates user info in the toolbar when activity is resumed
     */
    @Override
    protected void onResume() {
        updateUserInfo();
        super.onResume();
    }

    /**
     * Prevents going back to login on back pressed
     */
    @Override
    public void onBackPressed() {
        if (!PlayerManager.getInstance().isLoggedIn()) {
            super.onBackPressed();
        }
        // exit on back pressed
        PlayerManager.getInstance().onStop();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Updates the toolbar when activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();
        updateUserInfo();
    }

    /**
     * Save the player when the activity is stopped
     */
    @Override
    protected void onStop() {
        PlayerManager.getInstance().saveCurrentPlayer();
        super.onStop();
        PlayerManager.getInstance().onStop();
    }

    /**
     * Stops music service when application is killed
     */
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MusicService.class));
        super.onDestroy();
    }

    /**
     * Updates an element in the toolbar
     */
    @Override
    public void update(Observable o, Object arg) {
        updateUserInfo();
    }

    /**
     * Used to pause music when Quizee goes on background
     */
    @Override
    protected void onPause() {
        MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }
}
