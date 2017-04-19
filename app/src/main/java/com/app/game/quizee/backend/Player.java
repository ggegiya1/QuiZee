package com.app.game.quizee.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Maude on 2017-04-03.
 */

public class Player extends Observable implements Serializable {
    private String id;
    private String name;
    private String image;
    private boolean online;
    private List<Player> friends;
    private int level;
    private final Set<Category> categoriesPurchased = new HashSet<>();
    private final Set<Category> categoriesSelected = new HashSet<>();
    private final List<Achievement> achievements = new ArrayList<>();

    private AtomicInteger correctlyAnswered = new AtomicInteger(0);

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
        setChanged();
        notifyObservers();
    }

    public boolean buyCategory(Category category){
        if (category.getPrice() > this.getPointsEarned()){
            return false;
        }
        this.points.getAndAdd(0 - category.getPrice());
        categoriesPurchased.add(category);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean canBuy(Category category){
        return this.getPointsEarned() >= category.getPrice();
    }

    public Set<Category> getCategoriesPurchased() {
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
            setChanged();
            notifyObservers();
        }
    }

    public int getCorrectlyAnswered(){
        return this.correctlyAnswered.get();
    }

    public int getPointsEarned(){
        return points.get();
    }

    public Set<Category> getCategoriesSelected(){
        return this.categoriesSelected;
    }

    public void addSelectedCategory(Category category){
        this.categoriesSelected.add(category);
    }

    public void removeSelectedCategory(Category category){
        this.categoriesSelected.remove(category);
    }

    public void clearSelectedCategories(){
        this.categoriesSelected.clear();
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", online=" + online +
                ", friends=" + friends +
                ", level=" + level +
                ", categoriesPurchased=" + categoriesPurchased +
                ", categoriesSelected=" + categoriesSelected +
                ", achievements=" + achievements +
                ", correctlyAnswered=" + correctlyAnswered +
                ", points=" + points +
                '}';
    }
}
