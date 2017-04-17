package com.app.game.quizee.backend;

import com.app.game.quizee.R;

/**
 * Created by Maude on 2017-04-03.
 */

public class Category {
    private int id;
    private String name;
    private double price;
    private int imageId;
    
    Category(int c_id, String c_name, double c_price, int c_imageId){
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

    public static Category any(){
        return new Category(0, "", 0,  R.mipmap.ic_launcher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id == category.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
