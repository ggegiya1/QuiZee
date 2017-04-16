package com.app.game.quizee.backend;


/**
 * Created by Maude on 2017-04-03.
 */

public class Achievement {
    private int id_achiev;
    private String description;
    private int exp;
    private int money;

    public Achievement(int id,String text, int experience, int currency) {
        id_achiev = id;
        description = text;
        exp = experience;
        money = currency;
    }

    //Getters
    public int getID(){
        return id_achiev;
    }

    public String getDesc(){
        return description;
    }

    public int getXP(){
        return exp;
    }
    public int getMoney(){
        return money;
    }
}


