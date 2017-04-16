package com.app.game.quizee.backend;

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
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getImageId(){
        return imageId;
    }
}
