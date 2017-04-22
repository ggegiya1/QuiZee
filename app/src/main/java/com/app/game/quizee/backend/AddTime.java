package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.io.Serializable;

/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class AddTime implements GameItem {

    @Override
    public void apply(Game game) {
        game.addTime();
    }

    @Override
    public int getPrice() {
        return 100;
    }

    @Override
    public String getType() {
        return "+ 5 dec";
    }

    @Override
    public int getImageId() {
        return R.drawable.ic_addtime;
    }
}
