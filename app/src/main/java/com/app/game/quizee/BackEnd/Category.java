package com.app.game.quizee.BackEnd;

/**
 * Created by Maude on 2017-04-03.
 */

public class Category {
    private int id;
    private String name;
    private double price;

    public Category(int c_id, String c_name, double c_price){
        id = c_id;
        name = c_name;
        price = c_price;
    }

    //Getters
    public int get_id(){
        return id;
    }

    public String get_name(){
        return name;
    }

    public double get_price(){
        return price;
    }

}
