package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.io.Serializable;
import java.io.StringReader;

/**
 * Created by Maude on 2017-04-03.
 */

public class Category implements Serializable{
    private int id;
    private String name;
    private String displayName;
    private int price;
    private int imageId;

    public Category(int id, String name, String displayName, int price, int imageId){
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.price = price;
        this.imageId = imageId;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

    public int getImageId(){
        return imageId;
    }

    public static Category any(){
        return new Category(0, "", "", 0,  R.mipmap.ic_launcher);
    }



    public String getDisplayName() {
        return displayName;
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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageId=" + imageId +
                '}';
    }
}
