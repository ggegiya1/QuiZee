package com.app.game.quizee.backend;

/**
 * Created by trist on 2017-04-23.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AchievementManager {

    private final Map<Integer, Achievement> allAchievements;

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
        //AchievementManager cm = new AchievementManager();
        allAchievements.put(0,(new Achievement(0,"Welcome aboard!", 50, 5, "Play 5 games on QuiZee." )));
        allAchievements.put(1,(new Achievement(1,"Just freshin' up", 100, 20, "Play 20 games on QuiZee." )));
        allAchievements.put(2, (new Achievement (2,"We getting there", 150, 50, "Play 50 games on QuiZee." )));
        allAchievements.put(3, (new Achievement (3,"GG on Veteran", 200, 100, "Play 100 games on QuiZee." )));
        allAchievements.put(4, (new Achievement (4,"We got a wise one", 50, 5, "Answer 50 questions correctly." )));
        allAchievements.put(5, (new Achievement (5,"Call Einstein ASAP", 200, 100, "Answer 100 questions correctly." )));
        allAchievements.put(6, (new Achievement (6,"Your IQ is on fire!", 285, 200, "Answer 200 questions correctly." )));
        allAchievements.put(7, (new Achievement (7,"You gotta be cheating, for real", 1, 1, "Answer 500 questions correctly." )));
        allAchievements.put(8, (new Achievement (8,"You ever played BANG?", 20, 5, "Buy 5 bombs." )));
        allAchievements.put(9, (new Achievement (9,"Don't skip class to play!", 20, 5, "Buy 5 skip." )));
        allAchievements.put(10, (new Achievement (10,"Tick tock, time's up", 20, 5, "Buy 5 timeadd." )));
        allAchievements.put(11, (new Achievement (11,"Hint me up, Sherlock", 20, 5, "Buy 5 hints." )));
        //TODO: Achiev achat categorie
        allAchievements.put(12, (new Achievement (12,"Knowledge is expandable", 20, 5, "Buy 1 category." )));
        allAchievements.put(13, (new Achievement (13,"This fella likes to learn!", 20, 5, "Buy 10 categories." )));
        allAchievements.put(14, (new Achievement (14,"Ain't no subject he can't deal", 120, 200, "Buy all categories." )));
        allAchievements.put(15, (new Achievement (15,"The streak is real, mate", 50, 50, "Answer 10 questions in a row." )));
        //TODO: Achiev level up
        allAchievements.put(16, (new Achievement (16,"Give it up!", 50, 50, "Reach lvl 5." )));
        allAchievements.put(17, (new Achievement (17,"Pretty impressive", 50, 50, "Reach lvl 10." )));
        allAchievements.put(18, (new Achievement (18,"We got a grown up over here", 50, 50, "Reach lvl 20." )));
        allAchievements.put(19, (new Achievement (19,"Best of the bests, QuiZee Master", 50, 50, "Reach lvl 50." )));
    }


    public Achievement getAchievementByID(int achieID) {
        return this.allAchievements.get(achieID);
    }

    public Map getAllAchievements() {
        return allAchievements;
    }

    public ArrayList<Achievement> updateAchievements() {
        ArrayList<Achievement> completedAchievements = new ArrayList<>();

        return completedAchievements;
    }

}
