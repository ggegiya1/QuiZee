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

    private final List<Category> categoriesPurchased = new ArrayList<>();
    private final List<Category> categoriesSelected = new ArrayList<>();
    private final List<Achievement> achievements = new ArrayList<>();
    private final List<Skip> skips = new ArrayList<>();
    private final List<AddTime> addTimes = new ArrayList<>();
    private final List<Hint> hints = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>();

    private List<Question> correctlyAnswered = new ArrayList<>();
    private List<Question> wronglyAnswered = new ArrayList<>();

    private int points;
    private int level;
    private int score;

    public Player() {
    }

    public Player(String playerId, String name, String image, int level, int points){
        this.id = playerId;
        this.name = name;
        this.image = image;
        this.level = level;
        this.points = points;
    }

    public Player(String playerId, String name, String image, int level){
       new Player(playerId, name, image, level, 0);
    }

    public Player(String playerId, String name){
        new Player(playerId, name, null, 0, 0);
    }

    public static Player defaultPlayer() {
        Player bob = new Player("1", "Bob", null, 5, 1000);
        bob.getAddTimes().add(new AddTime());
        bob.getSkips().add(new Skip());
        bob.getHints().add(new Hint());
        bob.getHints().add(new Hint());
        bob.getHints().add(new Hint());
        bob.getHints().add(new Hint());
        bob.getBombs().add(new Bomb());
        bob.getBombs().add(new Bomb());
        bob.getBombs().add(new Bomb());
        return bob;
    }

    public void onGameStart(){
        this.correctlyAnswered.clear();
        this.wronglyAnswered.clear();
    }

    public void onGameEnd(){
        this.correctlyAnswered.clear();
        this.wronglyAnswered.clear();
    }

    private void addScore(int score) {
        this.score += score;
        this.points += score;
        setChanged();
        notifyObservers();
    }

    private void removeScore(int score) {
        this.score = this.score > score ? this.score - score : 0;
        this.points = this.points > score ? this.points - score : 0;
        setChanged();
        notifyObservers();
    }

    public boolean buyCategory(Category category) {
        if (category.getPrice() > this.getPoints()) {
            return false;
        }
        this.points -= category.getPrice();
        categoriesPurchased.add(category);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean canBuy(Category category) {
        return this.getPoints() >= category.getPrice();
    }

    public List<Category> getCategoriesPurchased() {
        return categoriesPurchased;
    }

    public boolean alreadyPurchased(Category category){
        return  categoriesPurchased.contains(category);
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public List<Player> getFriends() {
        return friends;
    }

    public boolean isFriend(Player p) {
        return friends.contains(p);
    }

    public boolean isOnline() {
        return online;
    }

    public void addFriend(Player p) {
        friends.add(p);
    }

    //Getters
    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void addCorrectAnswer(Question question) {
        this.correctlyAnswered.add(question);
        int score = question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1);
        addScore(score);
    }

    public void addIncorrectAnswer(Question question) {
        this.wronglyAnswered.add(question);
        // penalize incorrect question
        int score = question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1);
        removeScore(score);
    }

    public int getPoints(){
        return points;
    }

    public int getScore() {
        return score;
    }

    public List<Category> getCategoriesSelected(){
        return this.categoriesSelected;
    }

    public void addSelectedCategory(Category category) {
        this.categoriesSelected.add(category);
    }

    public void removeSelectedCategory(Category category) {
        this.categoriesSelected.remove(category);
    }

    public void clearSelectedCategories() {
        this.categoriesSelected.clear();
    }


    public List<Skip> getSkips() {
        return skips;
    }

    public List<AddTime> getAddTimes() {
        return addTimes;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean canPurchase(GameItem gameItem) {
        return this.points >= gameItem.getPrice();
    }

    public boolean purchase(GameItem gameItem) {
        if (!canPurchase(gameItem)) {
            return false;
        }
        if (gameItem instanceof Skip) {
            this.skips.add((Skip) gameItem);
        }
        if (gameItem instanceof Hint) {
            this.hints.add((Hint) gameItem);
        }
        if (gameItem instanceof AddTime) {
            this.addTimes.add((AddTime) gameItem);
        }
        if (gameItem instanceof Bomb) {
            this.bombs.add((Bomb) gameItem);
        }
        this.points -= gameItem.getPrice();
        setChanged();
        notifyObservers();
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setFriends(List<Player> friends) {
        this.friends = friends;
    }

    public List<Question> getCorrectlyAnswered() {
        return correctlyAnswered;
    }

    public List<Question> getWronglyAnswered() {
        return wronglyAnswered;
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
