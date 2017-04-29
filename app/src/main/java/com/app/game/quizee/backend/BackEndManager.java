package com.app.game.quizee.backend;

import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maude on 2017-04-06.
 */

public class BackEndManager {

    public BackEndManager(){

    }
    public static ArrayList<GameItem> mes_item = new ArrayList<GameItem>(){{
        add(new Bomb());
        add(new Skip());
        add(new AddTime());
        add(new Hint());
        }};

}
