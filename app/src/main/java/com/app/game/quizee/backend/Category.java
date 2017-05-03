package com.app.game.quizee.backend;
import com.app.game.quizee.R;
import java.io.Serializable;

/**
 *Categories classify questions
 */
public class Category implements Serializable{
    private int id;
    private String name;
    private String displayName;
    private int price;
    private int imageId;
    private double popularity;

    /**
     *Empty constructor for serialization
     */
    public Category() {
    }

    /**
     *Constructor of class
     */
    public Category(int id, String name, String displayName, int price, int imageId){
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.price = price;
        this.imageId = imageId;
    }

    public void played(){
        this.popularity++;
    }
    /**
     *Return new category object
     */
    public static Category any(){
        return new Category(0, "", "", 0,  R.mipmap.ic_launcher);
    }

    /**
     *Getters
     */
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
    public double getPopularity() {
        return popularity;
    }
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    public String getDisplayName() {
        return displayName;
    }

    /**
     *Check if 2 categories are equal considering they are objects
     */
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
