package com.app.game.quizee.backend;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maude on 2017-04-03.
 */

public class Player implements Serializable {
    private String id;
    private String name;
    private String image;
    private boolean online;
    private List<Player> friends;
    private int level;
    private final List<Category> categories;
    private final List<Achievement> achievements;
    private int score;

    public Player(String playerId, String name, String image){
        id = playerId;
        this.name = name;
        this.image = image;
        this.categories = new ArrayList<>();
        this.achievements = new ArrayList<>();
    }

    public void addScore(int score){
        this.score += score;
    }

    public boolean buyCategory(Category category){
        if (category.getPrice() > this.score){
            return false;
        }
        this.score -= category.getPrice();
        return true;
    }

    public boolean canBuy(Category category){
        return this.score >= category.getPrice();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public boolean hasCategory(Category category){
        return  categories.contains(category);
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public int getScore() {
        return score;
    }

    public List<Player> getFriends() {
        return friends;
    }

    public boolean isFriend(Player p){
        return friends.contains(p);
    }

    public boolean isOnline(){
        return online;
    }

    public void addFriend(Player p){
        friends.add(p);
    }

    //Getters
    public int getLevel(){
        return level;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }

}
