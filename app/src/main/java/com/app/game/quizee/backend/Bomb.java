package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.io.Serializable;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class Bomb implements GameItem {

    public Bomb() {
    }

    @Override
    public void apply(Game game) {
        game.deleteTwoIncorrectAnswers();
    }

    @Override
    public int getPrice() {
        return 300;
    }

    @Override
    public String getType() {
        return "50/50";
    }

    @Override
    public int getImageId() {
        return R.drawable.ic_bomb;
    }
}