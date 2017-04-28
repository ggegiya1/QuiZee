package com.app.game.quizee.backend;

import java.util.ArrayList;

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
    public static ArrayList<Achievement> updateAchievements(Player player){
        ArrayList<Achievement> achievements = new ArrayList<>();
        for (Achievement a: Achievement.values()){
            if (a.isAchieved(player)){
                player.addAchievement(a);
                achievements.add(a);
                player.setexp(a.getXP());
            }
        }
        return achievements;

    }
}
