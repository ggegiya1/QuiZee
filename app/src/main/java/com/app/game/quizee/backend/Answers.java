package com.app.game.quizee.backend;

/**
 * Created by Maude on 2017-04-03.
 */

public class Answers {
    private int id;
    private boolean good;
    private String text;

    public Answers(int a_id,boolean right_answer, String a_text){
        id = a_id;
        good = right_answer;
        text = a_text;
    }

    //Getters

    public boolean isAnswer(){
        return good;
    }

    public String getText(){
        return text;
    }
}
