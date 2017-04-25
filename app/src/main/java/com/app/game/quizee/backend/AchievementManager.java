package com.app.game.quizee.backend;

/**
 * Created by trist on 2017-04-23.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AchievementManager {

    private final Map<String, Achievement> allAchievements;

    private static AchievementManager instance;

    private AchievementManager() {
        allAchievements = new HashMap<>();
        init();
    }

    public static AchievementManager getInstance(){
        if (instance == null){
            instance = new AchievementManager();
        }
        return instance;
    }

    private void init(){
        AchievementManager cm = new AchievementManager();

       //add(new Achievement());
    }

    private void add(Achievement Achievement){
        allAchievements.put(Achievement.getDesc(), Achievement);
    }

    public Achievement getAchievementByName(String AchievementName) {
        return this.allAchievements.get(AchievementName);
    }

    public Map getAllAchievements() {
        return allAchievements;
    }

    public ArrayList<Achievement> updateAchievements() {
        ArrayList<Achievement> completedAchievements = new ArrayList<>();

        return completedAchievements;
    }

}
