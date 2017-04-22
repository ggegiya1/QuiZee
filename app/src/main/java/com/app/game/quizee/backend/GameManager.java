package com.app.game.quizee.backend;


/**
 * Created by ggegiya1 on 2017-04-22.
 */

public class GameManager {

    private Game game;

    private Player player;

    public GameManager(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void executeSkip(){
        if (!player.getSkips().isEmpty()) {
            player.getSkips().remove().apply(game);
        }
    }

    public void executeHint(){
        if (!player.getHints().isEmpty()){
            player.getHints().remove().apply(game);
        }
    }

    public void executeBomb(){
        if (!player.getBombs().isEmpty()){
            player.getBombs().remove().apply(game);
        }
    }

    public void executeTime(){
        if (!player.getAddTimes().isEmpty()){
            player.getAddTimes().remove().apply(game);
        }
    }
}
