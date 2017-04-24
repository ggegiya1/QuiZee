package com.app.game.quizee.backend;


import java.io.Serializable;

/**
 * Created by Maude on 2017-04-03.
 */

public class Achievement implements Serializable{
    private int id_achiev;
    private String description;
    private String information;
    private int exp;
    private int money;
    private int progress;
    private int maxProgress;
    private String key;

    public Achievement() {
    }

    public Achievement(int id, String text, int experience, int currency) {
        id_achiev = id;
        description = text;
        exp = experience;
        money = currency;
    }

    //constructeur dachievement avec information nominale
    public Achievement(int id,String text, int experience, int currency, String a_information) {
        id_achiev = id;
        description = text;
        exp = experience;
        money = currency;
        information = a_information;
    }

    //constructeur dachievement avec information ordinal
    public Achievement(int id,String text, int experience, int currency, int pg, int maxPg) {
        id_achiev = id;
        description = text;
        exp = experience;
        money = currency;
        progress = pg;
        maxProgress = maxPg;
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

    public String getInformation() {return information;}

    public int getProgress() {return progress;}

    public int getMaxProgress() {return maxProgress;}

    //Setters

    public void setProgress(int pg) {
        progress = pg;
    }
}


