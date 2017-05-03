package com.app.game.quizee.backend;
import java.io.Serializable;

public class Answer implements Serializable{

    private String text;
    private boolean correct;

    /**
     *Empty constructor for serialization
     */
    public Answer() {
    }

    /**
     *Constructor with nominal information
     */
    public Answer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() {
        return text;
    }
    public boolean isCorrect() {
        return correct;
    }
}
