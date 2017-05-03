package com.app.game.quizee.backend;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class creates a queue of Question object and can further add a question to it
 */
public class Questions {
    private final Queue<Question> questions = new ConcurrentLinkedQueue<>();

    public Questions() {}

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
