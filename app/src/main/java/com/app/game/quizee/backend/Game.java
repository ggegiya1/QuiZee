package com.app.game.quizee.backend;

/**
 *Interface to manage a game, implements the methods for power-ups
 */
public interface Game {
    void deleteTwoIncorrectAnswers();
    void addTime();
    void skipQuestion();
    void showHint();
    void init();
    void finish();
}
