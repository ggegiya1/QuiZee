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
    private final List<Category> categoriesPurchased = new ArrayList<>();
    private final List<Achievement> achievements = new ArrayList<>();

    private AtomicInteger correctlyAnswered;

    private AtomicInteger points;

    public Player(String playerId, String name, String image, int points, int level){
        this.id = playerId;
        this.name = name;
        this.image = image;
        this.level = level;
        this.points = new AtomicInteger(points);
    }

    public Player(String playerId, String name, String image, int level){
       new Player(playerId, name, image, 0, level);
    }

    public static Player defaultPlayer(){
        return new Player("1", "Bob", null, 1000, 5);
    }

    public void addPoints(int points){
        this.points.addAndGet(points);
    }

    public boolean buyCategory(Category category){
        if (category.getPrice() > this.getPointsEarned()){
            return false;
        }
        this.points.getAndAdd(0 - category.getPrice());
        categoriesPurchased.add(category);
        return true;
    }

    public boolean canBuy(Category category){
        return this.getPointsEarned() >= category.getPrice();
    }

    public List<Category> getCategoriesPurchased() {
        return categoriesPurchased;
    }

    public boolean hasCategory(Category category){
        return  categoriesPurchased.contains(category);
    }

    public List<Achievement> getAchievements() {
        return achievements;
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
