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

import com.app.game.quizee.BackEnd.Category;

import java.util.ArrayList;

import layout.CareerFragment;
import layout.HomeFragment;

public class Bottom_Navigation extends AppCompatActivity {
    //inspiré de https://github.com/jaisonfdo/BottomNavigation
    // pour le view pager et le bottom_navigation

    public static ArrayList<Category> mes_cate = new ArrayList<>();

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

        create_category();

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
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

    private void create_category() {
        //TODO PUT IN DB


        Category category = new Category(0, "General Knowledge", 0);
        mes_cate.add(category);
        Category category1 = new Category(1, "Entertainment: Music", 200);
        mes_cate.add(category1);
        Category category2 = new Category(2, "Entertainment: Video Games", 500);
        mes_cate.add(category2);
        Category category3 = new Category(3, "Science: Computers", 300);
        mes_cate.add(category3);
        Category category4 = new Category(4, "Geography", 100);
        mes_cate.add(category4);
        Category category5 = new Category(5, "History", 150);
        mes_cate.add(category5);
        Category category6 = new Category(6, "Art", 50);
        mes_cate.add(category6);
        Category category7 = new Category(7,"Entertainment: Books",100);
        mes_cate.add(category7);
        Category category8 = new Category(8,"Entertainment: Film",50);
        mes_cate.add(category8);
        Category category9 = new Category(9,"Entertainment: Musicals & Theatres",50);
        mes_cate.add(category9);
        Category category10 = new Category(10,"Entertainment: Television",100);
        mes_cate.add(category10);
        Category category11 = new Category(11,"Entertainment: Board Games",100);
        mes_cate.add(category11);
        Category category12 = new Category(12,"Science & Nature",200);
        mes_cate.add(category12);
        Category category13 = new Category(13,"Celebrities",200);
        mes_cate.add(category13);
        Category category14 = new Category(14,"Animals",100);
        mes_cate.add(category14);
        Category category15 = new Category(15,"Politics",300);
        mes_cate.add(category15);
        Category category16 = new Category(16,"Science: Mathematics",400);
        mes_cate.add(category16);
        Category category17 = new Category(17,"Mythology",0);
        mes_cate.add(category17);
        Category category18 = new Category(18,"Sports",0);
        mes_cate.add(category18);
        Category category19 = new Category(19,"Vehicles",0);
        mes_cate.add(category19);
        Category category20 = new Category(20,"Entertainment: Comics",0);
        mes_cate.add(category20);
        Category category21 = new Category(21,"Entertainment: Japanese Anime & Manga",0);
        mes_cate.add(category21);
        Category category22 = new Category(22,"Entertainment: Cartoon & Animations",0);
        mes_cate.add(category22);
        Category category23 = new Category(23,"Science: Gadgets",0);
        mes_cate.add(category23);

    }

}
