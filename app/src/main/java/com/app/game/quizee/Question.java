package com.app.game.quizee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ggegiya1 on 2/15/17.
 */

public class Question {

    private String category;
    private String question;
    private String difficulty;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        if (incorrectAnswers== null){
            incorrectAnswers = new ArrayList<>();
        }
        return incorrectAnswers;
    }

    public void addIncorrectAnswers(String... incorrectAnswers) {
        if (incorrectAnswers != null){
            Collections.addAll(getIncorrectAnswers(), incorrectAnswers);
        }
    }

    public List<String> getAnswers(boolean shuffle){
        List<String> answers = new ArrayList<>();
        answers.add(getCorrectAnswer());
        answers.addAll(getIncorrectAnswers());
        if (shuffle){
            long seed = System.nanoTime();
            Collections.shuffle(answers, new Random(seed));
        }
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}
