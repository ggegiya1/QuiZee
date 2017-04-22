package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.util.ArrayList;

/**
 * Created by Maude on 2017-04-06.
 */

public class BackEndManager {
    public static ArrayList<GameItem> mes_item = new ArrayList<>();
    public static ArrayList<Category> mes_cate = new ArrayList<>();

    public static void create_item(){
        mes_item.add(new Bomb());
        mes_item.add(new Skip());
        mes_item.add(new AddTime());
        mes_item.add(new Hint());
    }
}
