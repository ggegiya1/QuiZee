package com.app.game.quizee;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ggegiya1 on 2/16/17.
 */

public class QuestionActivity extends AppCompatActivity{

    TextView questionText;
    TextView categoryTitle;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Button correctlyAnswered;
    Button pointsEarned;
    Button skipButton;
    String correctAnswer;
    Timer timer;
    TimerTask elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        skipButton = (Button) findViewById(R.id.button_question_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestion();
            }
        });
        Button exitButton = (Button) findViewById(R.id.button_question_change_category);

        categoryTitle = (TextView) findViewById(R.id.text_category_title);
        questionText = (TextView) findViewById(R.id.text_question);
        correctlyAnswered = (Button) findViewById(R.id.button_correctly_answered);
        pointsEarned = (Button) findViewById(R.id.button_points_earned);

        answer1 = (Button) findViewById(R.id.button_response_1);
        answer1.setOnClickListener(answerValidator());
        answer2 = (Button) findViewById(R.id.button_response_2);
        answer2.setOnClickListener(answerValidator());
        answer3 = (Button) findViewById(R.id.button_response_3);
        answer3.setOnClickListener(answerValidator());
        answer4 = (Button) findViewById(R.id.button_response_4);
        answer4.setOnClickListener(answerValidator());
        timer = new Timer(true);
        newQuestion();

    }

    public class QuestionFetcher extends AsyncTask<String, Object, Question>{
        @Override
        protected Question doInBackground(String... params) {
            Question question = null;
            try {
                question = TriviaApi.getQuestion(params[0]);
            } catch (Exception e) {
                Log.e("activity.question", "Error fetching question", e);
            }
            return question;
        }

        @Override
        protected void onPostExecute(Question question) {
            Log.i("activity.question", "fetched question: " + question.toString());
            questionText.setText(question.getQuestion());
            categoryTitle.setText(question.getCategory());
            List<String> answers = question.getAnswers(true);
            answer1.setText(answers.get(0));
            answer2.setText(answers.get(1));
            answer3.setText(answers.get(2));
            answer4.setText(answers.get(3));
            correctAnswer = question.getCorrectAnswer();
            reinitializer();
        }
    }

    private View.OnClickListener answerValidator(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = ((Button)v).getText().toString();
                Log.i("activity.question", String.format("answer: %s", answer));
                if (answer.equals(correctAnswer)){
                    Log.i("activity.question", "answer is correct");
                    v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    UserProfile.getUserProfile("1").addCorrectAnswer();
//                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }else {
                    Log.i("activity.question", "answer is incorrect");
                    v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
//                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    UserProfile.getUserProfile("1").addIncorrectAnswer();
                }
                newQuestion();
            }
        };
    }

    private void newQuestion(){
        QuestionFetcher questionFetcher = new QuestionFetcher();
        questionFetcher.execute("");
    }

    private void reinitializer(){
//        Animation mAnimation = new AlphaAnimation(1, 0);
//        mAnimation.setDuration(200);
//        mAnimation.setInterpolator(new LinearInterpolator());
//                v.startAnimation(mAnimation);
        answer1.getBackground().clearColorFilter();
        answer2.getBackground().clearColorFilter();
        answer3.getBackground().clearColorFilter();
        answer4.getBackground().clearColorFilter();
        UserProfile userProfile = UserProfile.getUserProfile("1");
        correctlyAnswered.setText(String.valueOf(userProfile.getCorrectlyAnswered()));
        pointsEarned.setText(String.valueOf(userProfile.getPointsEarned()));
    }
}
