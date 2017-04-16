package com.app.game.quizee.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gia on 15/04/17.
 */

public class Questions {

    private final Queue<Question> questions = new ConcurrentLinkedQueue<>();

    public Questions() {
    }

    public Questions(List<Question> questions) {
        if (questions != null){
            this.questions.addAll(questions);
        }
    }

    public void withQuestions(Collection<Question> questions){
        if (questions != null){
            this.questions.addAll(questions);
        }
    }

    public void withQuestion(Question question){
        if (question != null){
            questions.add(question);
        }
    }

    public Question nextQuestion() {
        return questions.poll();
    }

    public boolean isEmpty(){
        return questions.isEmpty();
    }
}
