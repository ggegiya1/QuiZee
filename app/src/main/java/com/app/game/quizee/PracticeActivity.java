package com.app.game.quizee;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Question;

import java.util.Collections;
import java.util.List;

public class PracticeActivity extends AppCompatActivity{

    private final TriviaApi triviaApi = new TriviaApi(Collections.singletonList(Category.any()), 10, true);

    TextView correctlyAnsweredLabel;
    TextView questionCountLabel;

    //User interface attributes
    TextView questionTextSwitcher;
    Button answer1TextSwitcher;
    Button answer2TextSwitcher;
    Button answer3TextSwitcher;
    Button answer4TextSwitcher;
    List<Button> answerButtons;

    Button skipButton;

    TextView category;
    ImageView icon;
    int questionCount;
    int correctlyAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        questionTextSwitcher = (TextView) findViewById(R.id.text_question);
        questionCount = -1;

        category = (TextView) findViewById(R.id.caterogy_Textview);

        icon = (ImageView) findViewById(R.id.caterogy_Icon);
        correctlyAnsweredLabel = (TextView) findViewById(R.id.correct_answer_count_label);
        questionCountLabel = (TextView) findViewById(R.id.question_count_label);
        skipButton  = (Button) findViewById(R.id.practice_skip_question_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestion();
            }
        });

        answer1TextSwitcher = (Button) findViewById(R.id.practice_button_response_1);
        answer1TextSwitcher.setOnClickListener(answerValidator());
        answerButtons.add(answer1TextSwitcher);
        answer2TextSwitcher = (Button) findViewById(R.id.practice_button_response_2);
        answer2TextSwitcher.setOnClickListener(answerValidator());
        answerButtons.add(answer2TextSwitcher);
        answer3TextSwitcher = (Button) findViewById(R.id.practice_button_response_3);
        answer3TextSwitcher.setOnClickListener(answerValidator());
        answerButtons.add(answer3TextSwitcher);
        answer4TextSwitcher = (Button) findViewById(R.id.practice_button_response_4);
        answer4TextSwitcher.setOnClickListener(answerValidator());
        answerButtons.add(answer4TextSwitcher);
        newQuestion();
    }


    private class QuestionFetcher extends AsyncTask<Object, Object, Question>{

        @Override
        protected Question doInBackground(Object... params) {
            return triviaApi.getQuestion();
        }

        @Override
        protected void onPostExecute(Question newQuestion) {
            setQuestion(newQuestion);
        }
    }

    private View.OnClickListener answerValidator(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer answer = (Answer)v.getTag();
                if (answer.isCorrect()){
                    buttonEffect(v, Color.GREEN);
                    correctlyAnswered++;
                }else {
                    buttonEffect(v, Color.RED);
                }
                newQuestion();
            }
        };
    }

    private void newQuestion(){
        questionCount++;
        QuestionFetcher questionFetcher = new QuestionFetcher();
        questionFetcher.execute();
    }


    private void setQuestion(Question question){
        if (question == null){
            Context context = getApplicationContext();
            CharSequence text = "Error fetching question";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        //change le texte de la question
        questionTextSwitcher.setText(question.getText_question());

        List<Answer> answers = question.getAnswers(true);
        for (int i=0; i<answers.size(); i++){
            this.answerButtons.get(i).setText(answers.get(i).getText());
            this.answerButtons.get(i).setTag(answers.get(i));
            this.answerButtons.get(i).setBackground(getResources().getDrawable(R.color.answerButton));
        }
        //met un icone et un texte correspondant a la category
        category.setText(question.getCategory().getName());
        icon.setImageResource(question.getCategory().getImageId());
        correctlyAnsweredLabel.setText(String.valueOf(correctlyAnswered));
        questionCountLabel.setText(String.valueOf(questionCount));
    }

    //prevents quiting game accidently on back pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder Ad = new AlertDialog.Builder(this);
        Ad.setIcon(android.R.drawable.ic_dialog_alert);
        Ad.setMessage(R.string.AlertDialog_message);
        Ad.setTitle(R.string.AlertDialog_title);

        //actions when user says yes
        Ad.setPositiveButton(R.string.yes , new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        } );
        Ad.setNegativeButton(R.string.no, null);
        Ad.show();
    }

    // inspired by http://stackoverflow.com/questions/7175873/click-effect-on-button-in-android
    public void buttonEffect(View button, final int color){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
