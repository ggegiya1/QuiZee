package com.app.game.quizee.backend;


import java.io.Serializable;

/**
 * Created by Maude on 2017-04-03.
 */

public enum  Achievement implements Serializable {

    GAMES_5 ("Welcome aboard!", 50, 5, "Play 5 games on QuiZee."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbGamesPlayed();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbGamesPlayed() == 5 && !player.hasAchievement(GAMES_5);
        }
    },

    GAMES_20 ("Just freshin' up", 100, 20, "Play 20 games on QuiZee."){
        @Override
        public int getMaxValue() {
            return 20;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbGamesPlayed();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbGamesPlayed() == 20 && !player.hasAchievement(GAMES_20);
        }
    },
    GAMES_50 ("We getting there", 150, 50, "Play 50 games on QuiZee."){
        @Override
        public int getMaxValue() {
            return 50;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbGamesPlayed();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbGamesPlayed() == 50 && !player.hasAchievement(GAMES_50);
        }
    },
    GAMES_100 ("GG on Veteran", 200, 100, "Play 100 games on QuiZee."){
        @Override
        public int getMaxValue() {
            return 100;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbGamesPlayed();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbGamesPlayed() == 100 && !player.hasAchievement(GAMES_100);
        }
    },

    ANSWERS_50 ("We got a wise one", 50, 5, "Answer 50 questions correctly."){
        @Override
        public int getMaxValue() {
            return 50;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbQanswered();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbQanswered() == 50 && !player.hasAchievement(ANSWERS_50);
        }
    },
    ANSWERS_100 ("Call Einstein ASAP", 200, 100, "Answer 100 questions correctly."){

        @Override
        public int getMaxValue() {
            return 100;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbQanswered();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbQanswered() == 100 && !player.hasAchievement(ANSWERS_100);
        }
    },
    ANSWERS_200 ("Your IQ is on fire!", 285, 200, "Answer 200 questions correctly."){

        @Override
        public int getMaxValue() {
            return 200;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbQanswered();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbQanswered() == 200 && !player.hasAchievement(ANSWERS_200);
        }
    },
    ANSWERS_500 ("You gotta be cheating, for real",400, 500, "Answer 500 questions correctly."){
        @Override
        public int getMaxValue() {
            return 500;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbQanswered();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbQanswered() == 500 && !player.hasAchievement(ANSWERS_500);
        }
    },
    ANSWER_10_IN_ROW ("The streak is real, mate", 50, 50, "Answer 10 questions in a row."){
        @Override
        public int getMaxValue() {
            return 10;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getCorrectlyAnswered().size();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getCorrectlyAnswered().size() == 10 && !player.hasAchievement(ANSWER_10_IN_ROW);
        }
    },

    BOMBS_5 ("You ever played BANG?", 20, 5, "Buy 5 bombs."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbBombsBought();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbBombsBought() == 5 && !player.hasAchievement(BOMBS_5);
        }
    },
    SKIPS_5 ("Don't skip class to play!", 20, 5, "Buy 5 skip."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbSkipssBought();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbSkipssBought() == 5 && !player.hasAchievement(SKIPS_5);
        }
    },
    TIME_5 ("Tick tock, time's up", 20, 5, "Buy 5 timeadd."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbTimeBought();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbTimeBought() == 5 && !player.hasAchievement(TIME_5);
        }
    },
    HINTS_5 ("Hint me up, Sherlock", 20, 5, "Buy 5 hints."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getNbHintsBought();
        }

        @Override
        public boolean isAchieved(Player player){
            return player.getNbHintsBought() == 5 && !player.hasAchievement(HINTS_5);
        }
    },

    //TODO: Achiev achat categorie
    CATEGORY_1 ("Knowledge is expandable", 20, 5, "Buy 1 category."){
        @Override
        public int getMaxValue() {
            return 1;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getCategoriesPurchased().size();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getCategoriesPurchased().size() == 1 && !player.hasAchievement(CATEGORY_1);
        }
    },

    CATEGORY_10 ("This fella likes to learn!", 20, 5, "Buy 10 categories."){
        @Override
        public int getMaxValue() {
            return 10;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getCategoriesPurchased().size();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getCategoriesPurchased().size() == 10 && !player.hasAchievement(CATEGORY_10);
        }
    },

    CATEGORY_ALL ("Ain't no subject he can't deal", 120, 200, "Buy all categories."){
        @Override
        public int getMaxValue() {
            return 16;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getCategoriesPurchased().size();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getCategoriesPurchased().size() == 16 && !player.hasAchievement(CATEGORY_ALL);
        }
    },
    LEVEL_5("Give it up!", 50, 50, "Reach lvl 5."){
        @Override
        public int getMaxValue() {
            return 5;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getLevel();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getLevel() == 5 && !player.hasAchievement(LEVEL_5);
        }
    },
    LEVEL_10("Pretty impressive", 50, 50, "Reach lvl 10."){
        @Override
        public int getMaxValue() {
            return 10;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getLevel();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getLevel() == 10 && !player.hasAchievement(LEVEL_10);
        }
    },
    LEVEL_20("We got a grown up over here", 50, 50, "Reach lvl 20."){
        @Override
        public int getMaxValue() {
            return 20;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getLevel();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getLevel() == 20 && !player.hasAchievement(LEVEL_20);
        }
    },
    LEVEL_50("Best of the bests, QuiZee Master", 50, 50, "Reach lvl 50."){
        @Override
        public int getMaxValue() {
            return 50;
        }

        @Override
        public int getCurrentValue(Player player) {
            return player.getLevel();
        }

        @Override
        public boolean isAchieved(Player player) {
            return player.getLevel() == 50 && !player.hasAchievement(LEVEL_50);
        }
    };

    private String description;
    private String information;
    private int exp;
    private int money;
    private int progress;
    private int maxProgress;
    private String key;


    //constructeur dachievement avec information nominale
    Achievement(String text, int experience, int currency, String a_information) {
        description = text;
        exp = experience;
        money = currency;
        information = a_information;

    }
    //Getters
    public String getDesc(){
        return description;
    }

    public int getXP(){
        return exp;
    }
    public int getMoney(){
        return money;
    }

    public String getInformation() {return information;}

    public int getProgress() {return progress;}

    public int getMaxProgress() {return maxProgress;}

    public int getMaxValue(){return -1;}

    //Setters

    public void setProgress(int pg) {
        progress = pg;
    }

    public int getProg(Player player){
        return (int)(Math.round((getCurrentValue(player)/(double)getMaxValue())*100));
    }

    public int getCurrentValue(Player player){return -1;}

    public boolean isAchieved(Player player){
        return false;
    }
}


