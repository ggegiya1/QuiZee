package com.app.game.quizee.backend;

import android.util.SparseIntArray;

import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;
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
    private int totalscore;
    private int currentscore;
    private int highscore;

    private Map<String, Integer> perfCategories = new HashMap<>();

    public Player() {
    }

    public Player(String playerId, String name, String image, int level, int points){
        this.id = playerId;
        this.name = name;
        this.image = image;
        this.level = level;
        this.points = points;
        this.nbGamesPlayed = 0;
        this.nbQanswered = 0;
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
        currentscore=score;
        if (highscore < score){
            highscore = score;
        }
        setTotalscore(this.totalscore + score);
    }

    public int getCurrentScore(){
        return currentscore;
    }

    private void addPoints(int points){
        setPoints(this.points + points);
    }

    private void removePoints(int score) {
        setPoints(this.points > score ? this.points - score : 0);

    }

    public void setTotalscore(int totalscore) {
        this.nbGamesPlayed +=10;
        this.totalscore = totalscore;
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
        int score = question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1);
        int points = question.getDifficultyScore() * ((int) (question.getTimeRemained() / 2000) + 1);
        updatePerformCategory(question.getCategory());
        addScore(score);
        addPoints(points);
    }

    public void addIncorrectAnswer(Question question) {
        this.wronglyAnswered.add(question);
        // penalize incorrect question
        removePoints(-5);
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

    public int getTotalscore() {
        return totalscore;
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
        if (gameItem instanceof Bomb) {
            this.bombs.add((Bomb) gameItem);
        }
        if (gameItem instanceof Skip) {
            this.skips.add((Skip) gameItem);
        }
        if (gameItem instanceof AddTime) {
            this.addTimes.add((AddTime) gameItem);
        }
        if (gameItem instanceof Hint) {
            this.hints.add((Hint) gameItem);
        }


        this.points -= gameItem.getPrice();
        setChanged();
        notifyObservers();
        return true;
    }

    public int get_nbGamesPlayed(){
        return this.nbGamesPlayed;
    };
    public int get_nbQanswered(){
        return this.nbQanswered;
    };

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
        return totalscore;
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
                ", totalscore=" + totalscore +
                ", perfCategories=" + perfCategories +
                '}';
    }
}
