package com.app.game.quizee.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gia on 03/05/17.
 */

public class Score {

    private long timeLeft;
    private Map<Question.Difficulty, Integer> answers = new HashMap<>();

    public void addAnswer(Question.Difficulty difficulty){
        Integer value = getAnswers(difficulty);
        this.answers.put(difficulty, ++value);

    }

    public int getAnswers(Question.Difficulty difficulty){
        Integer value = this.answers.get(difficulty);
        return value == null? 0: value;
    }

    public int getScore(Question.Difficulty difficulty){
        Integer answers = getAnswers(difficulty);
        return answers * difficulty.getValueInt();
    }

    public int getTotalScore(){
        int total = 0;
        for (Question.Difficulty d: Question.Difficulty.values()){
            total += getScore(d);
        }
        return total;
    }

    public int getTimeBonus() {
        return (int)(timeLeft / 1000);
    }

    public void setTimeLeft(long time) {
        this.timeLeft = time;
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}
