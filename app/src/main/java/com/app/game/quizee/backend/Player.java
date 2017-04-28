package com.app.game.quizee.backend;

import android.content.SharedPreferences;
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
    //For achievements
    private int nbGamesPlayed;
    private int nbQanswered;

    private static  Player practicePlayer;

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
    private int currentscore;
    private int highestScore;
    private int exp;
    private String avatar;


    private int nbBombsBought;
    private int nbHintsBought;
    private int nbSkipssBought;
    private int nbTimeBought;
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
        this.exp=0;
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
        currentscore+=score;
        if (highestScore < score){
            highestScore = score;
        }
    }

    public int getCurrentScore(){
        return currentscore;
    }

    private void addPoints(int points){
        setPoints(this.points + points);
    }

    private void resetScore() {
        this.currentscore = 0;
        this.nbGamesPlayed +=1;
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
        setexp(question.getDifficultyScore() * ((int) (question.getTimeRemained() / 2000) + 1));
    }

    public void setexp(int exp){
        this.exp += exp;
        if (this.exp == 1000){
            this.level+=1;
            this.exp=0;
        }
        BackEndManager.updateAchievements(this);
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


    public Bitmap getAvatarBitmap(SharedPreferences preference) {
        //la conversion de string en bitmap vient de
        // http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        try {
            byte [] encodeByte= Base64.decode(avatar,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            Log.e("player", "error reading avatar", e);
            return null;
        }
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

    private boolean canPurchase(GameItem gameItem) {
        return this.points >= gameItem.getPrice();
    }

    public boolean purchase(GameItem gameItem) {
        if (!canPurchase(gameItem)) {
            return false;
        }
        if (gameItem instanceof Bomb) {
            this.bombs.add((Bomb) gameItem);
            nbBombsBought+=1;
        }
        if (gameItem instanceof Skip) {
            this.skips.add((Skip) gameItem);
            nbSkipssBought+=1;
        }
        if (gameItem instanceof AddTime) {
            this.addTimes.add((AddTime) gameItem);
            nbTimeBought+=1;
        }
        if (gameItem instanceof Hint) {
            this.hints.add((Hint) gameItem);
            nbHintsBought+=1;
        }


        this.points -= gameItem.getPrice();
        setChanged();
        notifyObservers();
        return true;
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

    public int getNumberItemPurchased(Class<? extends GameItem> itemClass){
        if (itemClass.isAssignableFrom(Bomb.class)){
            return getBombs().size();
        }
        if (itemClass.isAssignableFrom(Skip.class)){
            return getSkips().size();
        }
        if (itemClass.isAssignableFrom(Hint.class)){
            return getHints().size();
        }
        if (itemClass.isAssignableFrom(AddTime.class)){
            return getAddTimes().size();
        }
        return 0;
    }

    public int getNbBombsBought() {
        return nbBombsBought;
    }

    public void setNbBombsBought(int nbBombsBought) {
        this.nbBombsBought = nbBombsBought;
    }

    public int getNbHintsBought() {
        return nbHintsBought;
    }

    public void setNbHintsBought(int nbHintsBought) {
        this.nbHintsBought = nbHintsBought;
    }

    public int getNbSkipssBought() {
        return nbSkipssBought;
    }

    public void setNbSkipssBought(int nbSkipssBought) {
        this.nbSkipssBought = nbSkipssBought;
    }

    public int getNbTimeBought() {
        return nbTimeBought;
    }

    public void setNbTimeBought(int nbTimeBought) {
        this.nbTimeBought = nbTimeBought;
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
                ", categoriesPurchased=" + categoriesPurchased +
                ", categoriesSelected=" + categoriesSelected +
                ", achievements=" + achievements +
                ", skips=" + skips +
                ", addTimes=" + addTimes +
                ", hints=" + hints +
                ", bombs=" + bombs +
                ", correctlyAnswered=" + correctlyAnswered +
                ", wronglyAnswered=" + wronglyAnswered +
                ", points=" + points +
                ", level=" + level +
                ", highscore=" + highestScore +
                ", perfCategories=" + perfCategories +
                '}';
    }
}
