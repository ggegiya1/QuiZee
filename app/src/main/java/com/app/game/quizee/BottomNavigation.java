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

import com.app.game.quizee.backend.BackEndManager;

import com.app.game.quizee.layout.CareerFragment;
import com.app.game.quizee.layout.HomeFragment;

public class BottomNavigation extends AppCompatActivity {
    //inspiré de https://github.com/jaisonfdo/BottomNavigation
    // pour le view pager et le bottom_navigation

    private ViewPager viewPager;
    Fragment homeFragment;
    Fragment contactsFragment;
    Fragment shopFragment;
    Fragment careerFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Do not touch! Load the classes
        BackEndManager.create_item();

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


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        shopFragment = new ShopFragment();
        homeFragment = new HomeFragment();
        contactsFragment = new ContactsFragment();
        careerFragment = new CareerFragment();

        adapter.addFragment(shopFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(contactsFragment);
        adapter.addFragment(careerFragment);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); //fait partir sur le home
    }

    //on settings button clicked
    public void settingsActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }


}
