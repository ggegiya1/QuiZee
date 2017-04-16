package com.app.game.quizee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.app.game.quizee.backend.Category;

/**
 * Created by ggegiya1 on 2/15/17.
 */

public class Question {
    private int id_question;
    private Category category;
    private String text_question;
    private String difficulty;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public Question(int q_id, Category q_category, String q_text, String q_difficulty, String q_correct){
        id_question = q_id;
        category = q_category;
        text_question = q_text;
        difficulty = q_difficulty;
        correctAnswer = q_correct;
    }

    public Category getCategory() {
        return category;
    }

    public String getText_question() {
        return text_question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
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
                "category='" + category.getName() + '\'' +
                ", text_question='" + text_question + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}
