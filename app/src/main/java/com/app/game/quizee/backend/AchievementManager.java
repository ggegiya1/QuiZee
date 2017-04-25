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

        add(new Achievement(0,"Welcome aboard!", 50, 5, "Play 5 games on QuiZee." ));
        add(new Achievement(1,"Just freshin' up", 100, 20, "Play 20 games on QuiZee." ));
        add(new Achievement(2,"We getting there", 150, 50, "Play 50 games on QuiZee." ));
        add(new Achievement(3,"GG on Veteran", 200, 100, "Play 100 games on QuiZee." ));
        add(new Achievement(4,"We got a wise one", 50, 5, "Answer 50 questions correctly." ));
        add(new Achievement(5,"Call Einstein ASAP", 200, 100, "Answer 100 questions correctly." ));
        add(new Achievement(6,"Your IQ is on fire!", 285, 200, "Answer 200 questions correctly." ));
        add(new Achievement(7,"You gotta be cheating, for real", 1, 1, "Answer 500 questions correctly." ));
        add(new Achievement(8,"You ever played BANG?", 20, 5, "Buy 5 bombs." ));
        add(new Achievement(9,"Hint me up, Sherlock", 20, 5, "Buy 5 hints." ));
        add(new Achievement(10,"Tick tock, time's up", 20, 5, "Buy 5 timeadd." ));
        add(new Achievement(11,"Don't skip class to play!", 20, 5, "Buy 5 skip." ));
        add(new Achievement(12,"Knowledge is expandable", 20, 5, "Buy 1 category." ));
        add(new Achievement(13,"This fella likes to learn!", 20, 5, "Buy 10 categories." ));
        add(new Achievement(14,"Ain't no subject he can't deal", 120, 200, "Buy all categories." ));
        add(new Achievement(15,"The streak is real, mate", 50, 50, "Answer 10 questions in a row." ));
        add(new Achievement(16,"Give it up!", 50, 50, "Reach lvl 5." ));
        add(new Achievement(17,"Pretty impressive", 50, 50, "Reach lvl 10." ));
        add(new Achievement(18,"We got a grown up over here", 50, 50, "Reach lvl 20." ));
        add(new Achievement(19,"Best of the bests, QuiZee Master", 50, 50, "Reach lvl 50." ));
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
