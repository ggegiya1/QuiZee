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
 * IMPORTANT! This class will be serialized and stored in Firebase DB in JSON format
 * To avoid serialization errors, please implement both setter and getter for any new attribute
 */

public class Player extends Observable implements Serializable {
    /**
     * Player's attributes
     */
    private String id;
    private String name;
    private String image;
    private String avatar;
    private boolean online;
    private List<Player> friends;
    private Map<String, Integer> availablePowerUps = new HashMap<>();
    private Map<String, Integer> purchasedPowerUps = new HashMap<>();
    private int points;
    private int level;


    /**
     * Variables for achievements and ranking
     */
    private int nbGamesPlayed;
    private int nbQanswered;
    private int currentScore;
    private int highestScore;
    private final List<Achievement> achievements = new ArrayList<>();
    private static  Player practicePlayer;

    /**
     * Stored in the DB for sorting access
     */
    private int totalratio;
    private int totalScore;
    private int exp;

    /**
     * Category management
     */
    private final List<Category> categoriesPurchased = new ArrayList<>();
    private final List<Category> categoriesSelected = new ArrayList<>();
    private final List<Category> categoriesFavorites = new ArrayList<>();

    /**
     * Game variables
     */
    private List<Question> correctlyAnswered = new ArrayList<>();
    private List<Question> wronglyAnswered = new ArrayList<>();

    /**
     * Different constructors of class Player
     * Used for various reasons throughout the application
     */
    public Player() {}

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

    /**
     * Creates a player with 1 power-up of each
     */
    public static Player defaultPlayer() {
        if (practicePlayer == null){
            practicePlayer = new Player("1", "Practice Mode", null, 0, 0, 0);
            practicePlayer.addPowerUp(PowerUp.BOMB);
            practicePlayer.addPowerUp(PowerUp.SKIP);
            practicePlayer.addPowerUp(PowerUp.ADDTIME);
            practicePlayer.addPowerUp(PowerUp.HINT);
        }
        return practicePlayer;
    }

    /**
     * We clear the amount of good/bad question answered and the score
     */
    public void onGameReset(){
        this.resetScore();
        this.correctlyAnswered.clear();
        this.wronglyAnswered.clear();
    }

    /**
     * Add the score in param to our current score
     * If the current score beats our highscore, we update it
     */
    public void addScore(int score) {
        currentScore +=score;
        if (highestScore < currentScore){
            highestScore = currentScore;
        }
    }

    /**
     * Add the money to the total amount of money the player owns
     */
    private void addPoints(int points){
        setPoints(this.points + points);
    }

    /**
     * Put the current score back to 0 and change it on the UI
     */
    private void resetScore() {
        this.currentScore = 0;
        setChanged();
        notifyObservers();
    }

    /**
     * Check if the player has enough money to buy the category.
     * If so, we deduce the cost, add the category to the categories he bought
     * And update his achievements if necessary
     */
    public boolean buyCategory(Category category) {
        if (canBuy(category)){
            setPoints(this.points - category.getPrice());
            categoriesPurchased.add(category);
            updateAchievements();
            return true;
        }
        return false;
    }

    public boolean canBuy(Category category) {
        return this.getPoints() >= category.getPrice();
    }

    public boolean alreadyPurchased(Category category){
        return  categoriesPurchased.contains(category);
    }

    /**
     * The player has got a good answer, we add it to the list
     * We then update the categories from his preferences
     * We recalculate the player's score, money and exp accordingly
     */
    public void addCorrectAnswer(Question question) {
        this.correctlyAnswered.add(question);
        this.nbQanswered +=1;
        setChanged();
        notifyObservers();
    }

    /**
     * If the player has 1000 or over exp, we update his level and reset his exp
     */
    public void addexp(int exp){
        this.exp += exp;
        if (this.exp >= 1000){
            this.level+=1;
            this.exp=0;
        }
    }

    public void addmoney(int mon){
        this.points+=mon;
    }

    public void addIncorrectAnswer(Question question) {
        this.wronglyAnswered.add(question);
    }

    public boolean hasAchievement(Achievement achievement){
        return this.achievements.contains(achievement);
    }

    public void addAchievement(Achievement achievement){
        if (!this.achievements.contains(achievement)){
            this.achievements.add(achievement);
            setChanged();
            notifyObservers();
        }
    }
    /**
     * The conversion of string to bitmap comes from
     * http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     */
    public Bitmap avatarBitmap() {
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

    /**
     * Once selected, add the category to the favorites and increase the popularity
     */
    public void addSelectedCategory(Category category) {
        this.categoriesSelected.add(category);
        category.played();
        if (!this.categoriesFavorites.contains(category)){
            this.categoriesFavorites.add(category);
        }
    }

    public void clearSelectedCategories() {
        this.categoriesSelected.clear();
    }

    /**
     * Remove a power-up from the player's amount
     * Only call during a game
     */
    public void removePowerUp(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp.getName());
        if (current!=null && current > 0){
            this.availablePowerUps.put(powerUp.getName(), --current);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Add a power-up to the player's amount
     * Only called from the shop
     * Max is the total amount of that power-up purchased since the beginning
     * Current is the amount of that power-up the player holds right now
     */
    public void addPowerUp(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp.getName());
        Integer max = this.purchasedPowerUps.get(powerUp.getName());
        this.availablePowerUps.put(powerUp.getName(), current == null? 1: ++current);
        this.purchasedPowerUps.put(powerUp.getName(), max == null? 1: ++max);
        // Notify observers to reflect the changes in UI
        setChanged();
        notifyObservers();
    }

    public boolean canPurchase(PowerUp powerUp) {
        return this.points >= powerUp.getPrice();
    }

    /**
     * If the player has enough money to buy the power-up,
     * We lower his money and add the power-up to his list
     */
    public boolean purchase(PowerUp powerUp) {
        if (!canPurchase(powerUp)) {
            return false;
        }
        setPoints(this.points - powerUp.getPrice());
        addPowerUp(powerUp);
        return true;
    }

    /**
     *Setters
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setNbGamesPlayed(int nbGamesPlayed) {
        this.nbGamesPlayed = nbGamesPlayed;
    }
    public void setNbQanswered(int nbQanswered) {
        this.nbQanswered = nbQanswered;
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
    public void setTotalratio(int totalratio) {
        this.totalratio = totalratio;
        setChanged();
        notifyObservers();
    }
    /**
     * Modify the money of the player and update the amount visually
     */
    public void setPoints(int points){
        this.points = points;
        setChanged();
        notifyObservers();
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
        setChanged();
        notifyObservers();
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

    /**
     *Getters
     */
    public List<Achievement> getAchievements() {
        return achievements;
    }
    public List<Category> getCategoriesPurchased() {
        return categoriesPurchased;
    }
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
    public int getPoints(){
        return points;
    }
    public List<Category> getCategoriesSelected(){
        return this.categoriesSelected;
    }
    public String getAvatar() {
        return avatar;
    }
    public int getNbGamesPlayed(){
        return this.nbGamesPlayed;
    }
    public int getNbQanswered(){
        return this.nbQanswered;
    }
    public int getCurrentScore(){
        return currentScore;
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

    /**
     *Return 0 or the number of powerups that the player can buy
     */
    public int getNumberAvailablePowerUps(PowerUp powerUp){
        Integer current = this.availablePowerUps.get(powerUp.getName());
        return current == null? 0: current;
    }

    /**
     *Return 0 or the number of powerups bought by the player
     */
    public int getNumberPurchased(PowerUp powerUp){
        Integer current = this.purchasedPowerUps.get(powerUp.getName());
        return current == null? 0: current;
    }

    public int getExp() {
        return exp;
    }
    public int getTotalratio() {
        return totalratio;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public Map<String, Integer> getAvailablePowerUps() {
        return availablePowerUps;
    }
    public Map<String, Integer> getPurchasedPowerUps() {
        return purchasedPowerUps;
    }
    public List<Category> getCategoriesFavorites() {
        return categoriesFavorites;
    }

    /**
     * For every achievement, check if it is already achieved
     * If not, we add the achievement to the player's achievements list
     * We then add the achievement to an array list which will be returned
     * We finally add the exp and money related to the player
     */
    public List<Achievement> updateAchievements(){
        List<Achievement> achievements = new ArrayList<>();
        for (Achievement a: Achievement.values()){
            if (a.isAchieved(this)){
                this.addAchievement(a);
                achievements.add(a);
                this.addexp(a.getXP());
                this.addmoney(a.getMoney());
            }
        }
        return achievements;
    }

    /**
     * Allow to compare two Players using object comparison
     */
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
                '}';
    }
}
