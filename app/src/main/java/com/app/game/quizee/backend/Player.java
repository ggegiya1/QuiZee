package com.app.game.quizee.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Maude on 2017-04-03.
 */

public class Player extends Observable implements Serializable {
    private String id;
    private String name;
    private String image;
    private boolean online;
    private List<Player> friends;
    private Map<String, Integer> availablePowerUps = new HashMap<>();
    private Map<String, Integer> purchasedPowerUps = new HashMap<>();

    //For achievements
    private int nbGamesPlayed;
    private int nbQanswered;

    private static  Player practicePlayer;

    private final List<Category> categoriesPurchased = new ArrayList<>();
    private final List<Category> categoriesSelected = new ArrayList<>();

    private final List<Achievement> achievements = new ArrayList<>();

    private List<Question> correctlyAnswered = new ArrayList<>();
    private List<Question> wronglyAnswered = new ArrayList<>();

    private int points;
    private int level;
    private int currentScore;
    private int highestScore;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    private int totalScore;

    private int exp;
    private String avatar;

    private Map<String, Integer> perfCategories = new HashMap<>();

    public Player() {
    }

    public Player(String playerId, String name, String image, int level, int points, int exp){
        this.id = playerId;
        this.name = name;
        this.image = image;
        this.level = level;
        this.points = points;
        this.nbGamesPlayed = 0;
        this.nbQanswered = 0;
        this.exp=exp;
    }

    public Player(String playerId, String name, String image, int level){
       this(playerId, name, image, level, 0, 0);
    }

    public Player(String playerId, String name){
        this(playerId, name, null, 0, 0, 0);
    }

    public static Player defaultPlayer() {
        if (practicePlayer == null){
            practicePlayer = new Player("1", "Practice Mode", null, 0, 0, 0);
        }
        return practicePlayer;
    }
    public void onGameReset(){
        this.resetScore();
        this.correctlyAnswered.clear();
        this.wronglyAnswered.clear();
    }

    private void addScore(int score) {
        currentScore +=score;
        if (highestScore < score){
            highestScore = score;
        }
    }

    public int getCurrentScore(){
        return currentScore;
    }

    private void addPoints(int points){
        setPoints(this.points + points);
    }

    private void resetScore() {
        this.totalScore += this.currentScore;
        this.currentScore = 0;
        setChanged();
        notifyObservers();
    }

    public void setPoints(int points){
        this.points = points;
        setChanged();
        notifyObservers();
    }

    public boolean buyCategory(Category category) {
        if (category.getPrice() > this.getPoints()) {
            return false;
        }
        setPoints(this.points - category.getPrice());
        categoriesPurchased.add(category);
        updateAchievements();
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
        this.nbQanswered +=1;
        updatePerformCategory(question.getCategory());
        addScore(question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1));
        addPoints(question.getDifficultyScore() * ((int) (question.getTimeRemained() / 2000) + 1));
        addexp(question.getDifficultyScore() * ((int) (question.getTimeRemained() / 2000) + 1));
    }

    public void addexp(int exp){
        this.exp += exp;
        if (this.exp >= 1000){
            this.level+=1;
            this.exp=0;
        }
    }

    public void addIncorrectAnswer(Question question) {
        this.wronglyAnswered.add(question);
    }

    private void updatePerformCategory(Category category){
        Integer value = this.perfCategories.get(category.getName());
        if (value == null){
            value = 0;
        }
        this.perfCategories.put(category.getName(), ++value);

    }

    public boolean hasAchievement(Achievement achievement){
        return this.achievements.contains(achievement);
    }

    public void addAchievement(Achievement achievement){
        if (!this.achievements.contains(achievement)){
            this.achievements.add(achievement);
        }
    }
    public int getPoints(){
        return points;
    }

    public List<Category> getCategoriesSelected(){
        return this.categoriesSelected;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public Bitmap avatarBitmap() {
        //la conversion de string en bitmap vient de
        // http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        if (avatar!=null){
            try {
                byte [] encodeByte= Base64.decode(avatar,Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch(Exception e) {
                Log.e("player", "error reading avatar", e);
                return null;
            }
        }
        return null;
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

    public void removePowerUp(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp.getName());
        if (current!=null && current > 0){
            this.availablePowerUps.put(powerUp.getName(), --current);
        }
    }

    public void addPowerUp(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp);
        Integer max = this.purchasedPowerUps.get(powerUp);
        this.availablePowerUps.put(powerUp.getName(), current == null? 1: ++current);
        this.purchasedPowerUps.put(powerUp.getName(), current == null? 1: ++current);
    }

    public boolean canPurchase(PowerUp powerUp) {
        return this.points >= powerUp.getPrice();
    }

    public boolean purchase(PowerUp powerUp) {
        if (!canPurchase(powerUp)) {
            return false;
        }
        addPowerUp(powerUp);
        this.points -= powerUp.getPrice();
        setChanged();
        notifyObservers();
        return true;
    }

    public void setNbGamesPlayed(int nbGamesPlayed) {
        this.nbGamesPlayed = nbGamesPlayed;
    }

    public void setNbQanswered(int nbQanswered) {
        this.nbQanswered = nbQanswered;
    }

    public int getNbGamesPlayed(){
        return this.nbGamesPlayed;
    }

    public int getNbQanswered(){
        return this.nbQanswered;
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

    public int getHighestScore() {
        return highestScore;
    }

    public Map<String, Integer> getPrefCategories() {
        return perfCategories;
    }

    public int getNumberAvailablePowerUps(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp);
        return current == null? 0: current;
    }

    public int getNumberPurchased(PowerUp powerUp){
        Integer current = this.purchasedPowerUps.get(powerUp);
        return current == null? 0: current;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setAvailablePowerUps(Map<String, Integer> availablePowerUps) {
        this.availablePowerUps = availablePowerUps;
    }

    public void setPurchasedPowerUps(Map<String, Integer> purchasedPowerUps) {
        this.purchasedPowerUps = purchasedPowerUps;
    }

    public static void setPracticePlayer(Player practicePlayer) {
        Player.practicePlayer = practicePlayer;
    }

    public void setCorrectlyAnswered(List<Question> correctlyAnswered) {
        this.correctlyAnswered = correctlyAnswered;
    }

    public void setWronglyAnswered(List<Question> wronglyAnswered) {
        this.wronglyAnswered = wronglyAnswered;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setPerfCategories(Map<String, Integer> perfCategories) {
        this.perfCategories = perfCategories;
    }

    public Map<String, Integer> getPerfCategories() {
        return perfCategories;
    }

    public Map<String, Integer> getAvailablePowerUps() {

        return availablePowerUps;
    }

    public Map<String, Integer> getPurchasedPowerUps() {
        return purchasedPowerUps;
    }

    public List<Achievement> updateAchievements(){
        List<Achievement> achievements = new ArrayList<>();
        for (Achievement a: Achievement.values()){
            if (a.isAchieved(this)){
                this.addAchievement(a);
                achievements.add(a);
                this.addexp(a.getXP());
            }
        }
        return achievements;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id != null ? id.equals(player.id) : player.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", online=" + online +
                ", friends=" + friends +
                ", availablePowerUps=" + availablePowerUps +
                ", purchasedPowerUps=" + purchasedPowerUps +
                ", nbGamesPlayed=" + nbGamesPlayed +
                ", nbQanswered=" + nbQanswered +
                ", categoriesPurchased=" + categoriesPurchased +
                ", categoriesSelected=" + categoriesSelected +
                ", achievements=" + achievements +
                ", correctlyAnswered=" + correctlyAnswered +
                ", wronglyAnswered=" + wronglyAnswered +
                ", points=" + points +
                ", level=" + level +
                ", currentScore=" + currentScore +
                ", highestScore=" + highestScore +
                ", exp=" + exp +
                ", avatar='" + avatar + '\'' +
                ", perfCategories=" + perfCategories +
                '}';
    }
}
