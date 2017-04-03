package com.app.game.quizee.BackEnd;
import com.app.game.quizee.BackEnd.Player;
import java.util.ArrayList;
import java.util.List;
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
