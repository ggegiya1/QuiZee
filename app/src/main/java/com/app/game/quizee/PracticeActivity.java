package com.app.game.quizee;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PracticeActivity extends AppCompatActivity{

    private final TriviaApi triviaApi = new TriviaApi(Collections.singletonList(Category.any()), 10, true);

    TextView correctlyAnsweredLabel;
    TextView questionCountLabel;

    //User interface attributes
    TextSwitcher questionTextSwitcher;
    TextSwitcher answer1TextSwitcher;
    TextSwitcher answer2TextSwitcher;
    TextSwitcher answer3TextSwitcher;
    TextSwitcher answer4TextSwitcher;

    Button skipButton;

    Boolean answerable;
    String correctAnswer;
    List<TextSwitcher> TextSwitchers;
    TextView category;
    ImageView icon;
    int questionCount;
    int correctlyAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        answerable = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        questionTextSwitcher = (TextSwitcher) findViewById(R.id.text_question);
        questionCount = -1;

        //ajoute les view a créé lors de lanimation de changement de texte du questionTextSwitcher
        //viewfactory tiré de la page http://www.androhub.com/android-textswitcher/
        questionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                //crer un TextView avec des caractéristiques
                TextView myText = new TextView(PracticeActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                myText.setBackgroundDrawable(getResources().getDrawable((R.drawable.button_secondary_default)));
                return myText;
            }
        });

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

        answer1TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_1);
        answer2TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_2);
        answer3TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_3);
        answer4TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_4);

        TextSwitchers = new ArrayList<>();
        TextSwitchers.add(answer1TextSwitcher);
        TextSwitchers.add(answer2TextSwitcher);
        TextSwitchers.add(answer3TextSwitcher);
        TextSwitchers.add(answer4TextSwitcher);

        //ajoute le viewFactory pour les animations des boutons
        for (TextSwitcher ts: TextSwitchers) {
            ts.setFactory(
                    new ViewSwitcher.ViewFactory() {

                        public View makeView() {
                            //crer un Button avec des caractéristiques
                            Button myButton = new Button(PracticeActivity.this);
                            myButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                            myButton.setTextSize(15);
                            myButton.setOnClickListener(answerValidator());
                            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            myButton.setLayoutParams(lp);
                            myButton.setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
                            return myButton;
                        }
                    });
        }
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
                if(answerable) {
                    String answer = ((Button)v).getText().toString();
                    Log.i("activity.question", String.format("answer: %s", answer));
                    if (answer.equals(correctAnswer)){
                        Log.i("activity.question", "answer is correct");

                        v.setBackgroundColor(Color.GREEN);
                        correctlyAnswered++;
                    }else {
                        Log.i("activity.question", "answer is incorrect");
                        v.setBackgroundColor(Color.RED);
                    }
                    newQuestion();
                }
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

        //ajuste le taille du texte pour que le texte ne depasse pas
        TextView tv1 = (TextView) questionTextSwitcher.getChildAt(0);
        TextView tv2 = (TextView) questionTextSwitcher.getChildAt(1);
        if(questionCount%2 == 1) {
            tv1.setTextSize(40 - question.getText_question().length() / 6);
        } else {
            tv2.setTextSize(40 - question.getText_question().length() / 6);
        }

        questionTextSwitcher.setText(question.getText_question());
        List<String> answers = question.getAnswers(true);

        //efface les couleurs sur les boutons et mets le texte correspondant aux reponses sur les boutons
        for (int i=0; i<TextSwitchers.size(); i++) {
            TextSwitcher ts = TextSwitchers.get(i);
            ts.setText(answers.get(i));
            if (questionCount % 2 == 0) {
                ts.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
            } else {
                ts.getChildAt(1).setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
            }
        }

        //met un icone et un texte correspondant a la category
        category.setText(question.getCategory().getName());
        icon.setImageResource(question.getCategory().getImageId());
        correctAnswer = question.getCorrectAnswer();
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
}
