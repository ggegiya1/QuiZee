package com.app.game.quizee.backend;

import com.app.game.quizee.R;

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
    public String getName(){return "Bomb";}

    @Override
    public int getPosition(){return 1;}

    @Override
    public int getImageId() {
        return R.drawable.ic_bomb;
    }

    @Override
    public int getDescription() {
        return R.string.description_bomb;
    }
}
