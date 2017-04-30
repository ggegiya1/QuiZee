package com.app.game.quizee.backend;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public interface Game {
    void deleteTwoIncorrectAnswers();
    void addTime();
    void skipQuestion();
    void showHint();
}
