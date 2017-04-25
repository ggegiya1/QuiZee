package com.app.game.quizee.backend;

import java.io.Serializable;

/**
 * Abstract class for power-ups
 */

public interface GameItem extends Serializable{

    int getPrice();

    String getType();

    int getImageId();

    String getName();

    void apply(Game game);

    int getPosition();

}
