package com.app.game.quizee.backend;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Maude on 2017-04-03.
 */

public class Player extends Observable implements Serializable {
    private String id;
    private String name;
    private String image;
    private boolean online;
    private List<Player> friends;

    private final Set<Category> categoriesPurchased = new HashSet<>();
    private final Set<Category> categoriesSelected = new HashSet<>();
    private final List<Achievement> achievements = new ArrayList<>();
    private final Queue<Skip> skips = new ArrayDeque<>();
    private final Queue<AddTime> addTimes = new ArrayDeque<>();
    private final Queue<Hint> hints = new ArrayDeque<>();
    private final Queue<Bomb> bombs = new ArrayDeque<>();

    private List<Question> correctlyAnswered;
    private List<Question> wronglyAnswered;

    private int points;
    private int level;
    private int score;

    public Player(String playerId, String name, String image, int level, int points){
        this.id = playerId;
        this.name = name;
        this.image = image;
        this.level = level;
        this.points = points;
        this.correctlyAnswered = new ArrayList<>();
        this.wronglyAnswered = new ArrayList<>();
    }

    public Player(String playerId, String name, String image, int level){
       new Player(playerId, name, image, 0, level);
    }

    public static Player defaultPlayer(){
        Player bob = new Player("1", "Bob", null, 1000, 5);
        bob.getAddTimes().add(new AddTime());
        bob.getSkips().add(new Skip());
        bob.getHints().add(new Hint());
        bob.getBombs().add(new Bomb());
        return bob;
    }

    public void prepareForGame(){
        this.correctlyAnswered = new ArrayList<>();
        this.wronglyAnswered = new ArrayList<>();
    }

    private void addScore(int score){
        this.score += score;
        this.points += score;
        setChanged();
        notifyObservers();
    }

    private void removeScore(int score){
        this.score = this.score > score? this.score - score: 0;
        this.points = this.points > score? this.points - score: 0;
        setChanged();
        notifyObservers();
    }

    public boolean buyCategory(Category category){
        if (category.getPrice() > this.getPoints()){
            return false;
        }
        this.points -= category.getPrice();
        categoriesPurchased.add(category);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean canBuy(Category category){
        return this.getPoints() >= category.getPrice();
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

    public void addCorrectAnswer(Question question){
        this.correctlyAnswered.add(question);
        int score = question.getDifficultyScore() * ((int)(question.getTimeRemained()/1000) + 1);
        addScore(score);
    }

    public void addIncorrectAnswer(Question question){
        this.wronglyAnswered.add(question);
        // penalize incorrect question
        int score = question.getDifficultyScore() * ((int)(question.getTimeRemained()/1000) + 1) / 5;
        removeScore(score);
    }

    public int getCorrectlyAnswered(){
        return this.correctlyAnswered.size();
    }

    public int getWronglyAnswered(){
        return this.wronglyAnswered.size();
    }

    public int getPoints(){
        return points;
    }

    public int getScore() {
        return score;
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


    public Queue<Skip> getSkips() {
        return skips;
    }

    public Queue<AddTime> getAddTimes() {
        return addTimes;
    }

    public Queue<Hint> getHints() {
        return hints;
    }

    public Queue<Bomb> getBombs() {
        return bombs;
    }

    public boolean canPurchase(GameItem gameItem){
        return this.points >= gameItem.getPrice();
    }

    public boolean purchase(GameItem gameItem){
        if (!canPurchase(gameItem)){
            return false;
        }
        if (gameItem instanceof Skip){
            this.skips.add((Skip)gameItem);
        }
        if (gameItem instanceof Hint){
            this.hints.add((Hint)gameItem);
        }
        if (gameItem instanceof AddTime){
            this.addTimes.add((AddTime) gameItem);
        }
        if (gameItem instanceof Bomb){
            this.bombs.add((Bomb) gameItem);
        }
        this.points -= gameItem.getPrice();
        setChanged();
        notifyObservers();
        return true;
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
