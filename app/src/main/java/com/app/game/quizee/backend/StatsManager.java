package com.app.game.quizee.backend;
import java.util.ArrayList;

/**
 * Created by Maude on 2017-04-03.
 */

public class StatsManager {
    private ArrayList<Integer> achiev;
    private Player my_player;

    public StatsManager(Player p){
        my_player = p;
    }

    public void addAchievements(int id){
        //On fetch les achie par leur ID seulement
        achiev.add(id);
    }

    public ArrayList<Integer> getAchie(){
        return achiev;
    }

}
