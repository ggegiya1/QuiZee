package com.app.game.quizee.backend;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ggegiya1 on 2/15/17.
 */

public class Question implements Serializable {

    public enum Difficulty implements Serializable{
        easy (2, Color.GREEN),
        medium (4, Color.YELLOW),
        hard (6, Color.RED),
        unknown (1, Color.WHITE);

        private final int valueInt;
        private final int color;

        Difficulty(int valueInt, int color) {
            this.valueInt = valueInt;
            this.color = color;
        }

        static Difficulty fromString(String difficulty){
            for (Difficulty d: Difficulty.values()){
                if (d.name().equalsIgnoreCase(difficulty)){
                    return d;
                }
            }
            return unknown;
        }

        public int getValueInt() {
            return valueInt;
        }

        public int getColor() {
            return color;
        }
    }

    private Category category;
    private String text_question;
    private Difficulty difficulty;
    private List<Answer> answers;
    private long timeRemained;

    public Question() {
        difficulty = Difficulty.unknown;
    }

    public Question(Category category, String text, String difficulty){
        this.category = category;
        this.text_question = text;
        this.difficulty = Difficulty.fromString(difficulty);
        this.answers = new ArrayList<>();
    }

    public Category getCategory() {
        return category;
    }

    public String getTextQuestion() {
        return text_question;
    }

    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }

    public List<Answer> getAnswers(boolean shuffle){
        List<Answer> answers = new ArrayList<>(this.answers);
        if (shuffle){
            long seed = System.nanoTime();
            Collections.shuffle(answers, new Random(seed));
        }
        return answers;
    }

    public long getTimeRemained() {
        return timeRemained;
    }

    public void setTimeRemained(long timeRemained) {
        this.timeRemained = timeRemained;
    }

    public int getDifficultyScore(){
        return this.difficulty.getValueInt();
    }

    public Difficulty getDifficulty() {
        return difficulty;
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
