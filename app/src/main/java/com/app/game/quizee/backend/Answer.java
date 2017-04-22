package com.app.game.quizee.backend;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class Answer {

    private String text;

    private boolean correct;

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
