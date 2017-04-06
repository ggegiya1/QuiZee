package com.app.game.quizee.BackEnd;

/**
 * Created by Maude on 2017-04-03.
 */

public class Category {
    private int id;
    private String name;
    private double price;
    private int imageId;

    public Category(int c_id, String c_name, double c_price, int c_imageId){
        id = c_id;
        name = c_name;
        price = c_price;
        imageId = c_imageId;
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

    public int get_imageId(){
        return imageId;
    }
}
