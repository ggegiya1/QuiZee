package com.app.game.quizee.backend;

import com.app.game.quizee.R;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class Skip implements GameItem {
    public Skip() {
    }

    @Override
    public int getPrice() {
        return 100;
    }

    @Override
    public String getType() {
        return "Skip";
    }

    @Override
    public void apply(Game game) {
        game.newQuestion();
    }

    @Override
    public String getName(){return "Skip";}

    @Override
    public int getPosition(){return 2;}

    @Override
    public int getDescription() {
        return R.string.description_skip;
    }

    @Override
    public int getImageId() {
        return R.drawable.ic_skip;
    }
}
