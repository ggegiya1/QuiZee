package com.app.game.quizee;

import com.app.game.quizee.BackEnd.BackEndManager;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ggegiya1 on 2/16/17.
 */

public class UserProfile {

    private static UserProfile userProfile;

    private AtomicInteger correctlyAnswered;

    private AtomicInteger points;
    //TODO : Mettre en atomic integer
    private Hashtable<String,Integer> nb_items = new Hashtable <String,Integer>();
    private String id;

    private UserProfile(String id){
        this.id = id;
        correctlyAnswered = new AtomicInteger(0);
        points = new AtomicInteger(0);
        nb_items.put("Bomb", 0);
        nb_items.put("Skip", 0);
        nb_items.put("Time", 0);
    }

    public static UserProfile getUserProfile(String id){
        if (userProfile == null){
            userProfile = new UserProfile(id);
        }
        return userProfile;
    }

    public int get_amount(String name){
        return nb_items.get(name);
    }

    public void buy_one (String name){
        nb_items.put(name, nb_items.get(name)+1);
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
