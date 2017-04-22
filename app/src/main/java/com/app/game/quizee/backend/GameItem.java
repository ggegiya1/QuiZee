package com.app.game.quizee.backend;

import java.io.Serializable;

/**
 * Abstract class for power-ups
 */

public interface GameItem extends Serializable{

    int getPrice();

    String getType();

    int getImageId();

    void apply(Game game);


}
