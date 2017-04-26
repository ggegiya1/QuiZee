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
            Skip skip = player.getSkips().iterator().next();
            player.getSkips().remove(0);
            skip.apply(game);
        }
    }

    public void executeHint(){
        if (!player.getHints().isEmpty()){
            Hint hint = player.getHints().iterator().next();
            player.getHints().remove(0);
            hint.apply(game);
        }
    }

    public void executeBomb(){
        if (!player.getBombs().isEmpty()){
            Bomb bomb = player.getBombs().iterator().next();
            player.getBombs().remove(0);
            bomb.apply(game);
        }
    }

    public void executeTime(){
        if (!player.getAddTimes().isEmpty()){
            AddTime addTime = player.getAddTimes().iterator().next();
            player.getAddTimes().iterator().remove();
            addTime.apply(game);
        }
    }

    public int goodAnswerScore(Question question){
        return question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1);
    }

    public int wrongAnswerScore(Question question){
        return (-1) * question.getDifficultyScore() * ((int) (question.getTimeRemained() / 1000) + 1);
    }
}
