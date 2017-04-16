package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.util.ArrayList;

/**
 * Created by Maude on 2017-04-06.
 */

public class BackEndManager {
    public static ArrayList<Item> mes_item = new ArrayList<>();
    public static ArrayList<Category> mes_cate = new ArrayList<>();

    public static void create_item(){
        mes_item.add(new Item("Bomb", 50, R.drawable.ic_bomb));
        mes_item.add(new Item("Skip", 250, R.drawable.ic_skip));
        mes_item.add(new Item("Time", 150, R.drawable.ic_addtime));
        mes_item.add(new Item("Hint", 100, R.drawable.ic_hint));
    }
}
