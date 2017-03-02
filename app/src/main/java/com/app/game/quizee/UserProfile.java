package com.app.game.quizee;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ggegiya1 on 2/16/17.
 */

public class UserProfile {

    private static UserProfile userProfile;

    private AtomicInteger correctlyAnswered;

    private AtomicInteger points;

    private String id;

    private UserProfile(String id){
        this.id = id;
        correctlyAnswered = new AtomicInteger(0);
        points = new AtomicInteger(0);
    }

    public static UserProfile getUserProfile(String id){
        if (userProfile == null){
            userProfile = new UserProfile(id);
        }
        return userProfile;
    }

    public void addCorrectAnswer(){
        points.addAndGet(correctlyAnswered.incrementAndGet() * 5);
    }

    public void addIncorrectAnswer(){
        if (points.get() > 0) {
            points.decrementAndGet();
        }
    }

    public int getCorrectlyAnswered(){
        return this.correctlyAnswered.get();
    }

    public int getPointsEarned(){
        return points.get();
    }
}
