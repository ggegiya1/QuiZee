package com.app.game.quizee.backend;
import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question implements Serializable {

    /**
     * Enumeration for the difficulties of the questions, their color and their value
     */
    public enum Difficulty implements Serializable{
        easy (2, Color.GREEN),
        medium (4, Color.YELLOW),
        hard (6, Color.RED),
        unknown (1, Color.WHITE);

        private final int valueInt;
        private final int color;

        /**
         * Constructor of the Difficulty class
         */
        Difficulty(int valueInt, int color) {
            this.valueInt = valueInt;
            this.color = color;
        }

        /**
         * Return the difficulty object associated with it's name
         */
        static Difficulty fromString(String difficulty){
            for (Difficulty d: Difficulty.values()){
                if (d.name().equalsIgnoreCase(difficulty)){
                    return d;
                }
            }
            return unknown;
        }

        /**
         * Getters
         */
        public int getValueInt() {
            return valueInt;
        }
        public int getColor() {
            return color;
        }
    }

    /**
     * Attributes of Question class
     */
    private Category category;
    private String text_question;
    private Difficulty difficulty;
    private List<Answer> answers;
    private long timeRemained;

    /**
     * Empty constructor
     */
    public Question() {
        difficulty = Difficulty.unknown;
    }

    public Question(Category category, String text, String difficulty){
        this.category = category;
        this.text_question = text;
        this.difficulty = Difficulty.fromString(difficulty);
        this.answers = new ArrayList<>();
    }


    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }

    /**
     * Return the possible answers for the question and mix them if needed
     */
    public List<Answer> getAnswers(boolean shuffle){
        List<Answer> answers = new ArrayList<>(this.answers);
        if (shuffle){
            long seed = System.nanoTime();
            Collections.shuffle(answers, new Random(seed));
        }
        return answers;
    }

    /**
     * Getters
     */
    public long getTimeRemained() {
        return timeRemained;
    }
    public Category getCategory() {
        return category;
    }
    public String getTextQuestion() {
        return text_question;
    }
    public int getDifficultyScore(){
        return this.difficulty.getValueInt();
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Setters
     */
    public void setTimeRemained(long timeRemained) {
        this.timeRemained = timeRemained;
    }

    @Override
    public String toString() {
        return "Question{" +
                "category=" + category +
                ", text_question='" + text_question + '\'' +
                ", difficulty=" + difficulty +
                ", answers=" + answers +
                ", timeRemained=" + timeRemained +
                '}';
    }
}
