package com.app.game.quizee.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final List<Category> categories = new ArrayList<>();
    private final List<Achievement> achievements = new ArrayList<>();
    private int score;

    private AtomicInteger correctlyAnswered;

    private AtomicInteger points;

    public Player(String playerId, String name, String image){
        new Player(playerId, name, image, 0);
    }

    public Player(String p_id, String p_name, String p_img, int p_level){
        this.id = p_id;
        this.name = p_name;
        this.image = p_img;
        this.level = p_level;
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
