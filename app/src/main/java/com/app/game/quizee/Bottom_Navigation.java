package com.app.game.quizee;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import layout.HomeFragment;
import layout.StatsFragment;

public class Bottom_Navigation extends AppCompatActivity {
    //inspiré de https://github.com/jaisonfdo/BottomNavigation pour les view pager et le buttom navigation


    private ViewPager viewPager;
    Fragment homeFragment;
    Fragment statsFragment;
    Fragment contactsFragment;
    Fragment archivementsFragment;
    Fragment shopFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__navigation);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);



        viewPager = (ViewPager) findViewById(R.id.viewpager);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_shop:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_archivements:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_stats:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.navigation_contacts:
                                viewPager.setCurrentItem(4);
                                break;
                        }
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
                else
                {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();
        archivementsFragment = new AchivementsFragment();
        statsFragment = new StatsFragment();
        contactsFragment = new ContactsFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(shopFragment);
        adapter.addFragment(archivementsFragment);
        adapter.addFragment(statsFragment);
        adapter.addFragment(contactsFragment);
        viewPager.setAdapter(adapter);
    }

    public void categoriesPlay(View v) {
        Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
        startActivity(intent);
    }

    public void quickPlay(View v) {
        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        startActivity(intent);
    }

    public void multiPlay(View v) {
        Intent intent = new Intent(getApplicationContext(), MultiplayerLobbyActivity.class);
        startActivity(intent);
    }

    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

}
