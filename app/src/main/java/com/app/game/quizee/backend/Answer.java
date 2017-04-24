package com.app.game.quizee.backend;

import java.io.Serializable;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class Answer implements Serializable{

    private String text;

    private boolean correct;

    public Answer() {
    }

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
