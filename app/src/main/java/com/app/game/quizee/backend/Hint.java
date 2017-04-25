package com.app.game.quizee.backend;

import com.app.game.quizee.R;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class Hint implements GameItem {
    public Hint() {
    }

    @Override
    public int getPrice() {
        return 75;
    }

    @Override
    public String getType() {
        return "Hint";
    }

    @Override
    public void apply(Game game) {
        game.showHint();
    }

    @Override
    public String getName(){return "Hint";}

    @Override
    public int getPosition(){return 4;}

    @Override
    public int getImageId() {
        return R.drawable.ic_hint;
    }
}
