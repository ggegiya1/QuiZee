package com.app.game.quizee;

import android.annotation.TargetApi;
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
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity{

    //User interface attributes
    TextSwitcher questionTextSwitcher;
    TextSwitcher answer1TextSwitcher;
    TextSwitcher answer2TextSwitcher;
    TextSwitcher answer3TextSwitcher;
    TextSwitcher answer4TextSwitcher;

    //answer buttons
    Button correctlyAnswered;
    Button pointsEarned;

    //power ups buttons
    ImageButton skipButton;
    ImageButton addTimeButton;


    String correctAnswer;
    ProgressBar pb;
    MyCountDownTimer countDownTimer;
    List<TextSwitcher> TextSwitchers;
    TextView category;
    ImageView icon;
    int questionCount;

    final int baseTime = 10000; // temps entre les questions en milisecondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        addTimeButton = (ImageButton) findViewById(R.id.button_add_time);

        skipButton = (ImageButton) findViewById(R.id.button_question_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestion();
            }
        });
        questionTextSwitcher = (TextSwitcher) findViewById(R.id.text_question);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setRotation(180);
        questionCount = -1;

        //ajoute les view a créé lors de lanimation de changement de texte du questionTextSwitcher
        //viewfactory tiré de la page http://www.androhub.com/android-textswitcher/
        questionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                //crer un TextView avec des caractéristiques
                TextView myText = new TextView(QuestionActivity.this);
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
        correctlyAnswered = (Button) findViewById(R.id.button_correctly_answered);
        pointsEarned = (Button) findViewById(R.id.button_points_earned);

        answer1TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_1);
        answer2TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_2);
        answer3TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_3);
        answer4TextSwitcher = (TextSwitcher) findViewById(R.id.button_response_4);

        TextSwitchers = new ArrayList<TextSwitcher>();
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
                            Button myButton = new Button(QuestionActivity.this);
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

    private class QuestionFetcher extends AsyncTask<String, Object, Question>{
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
            // TODO trouver le bug de la ligne :Log.i("activity.question", "fetched question: " + question.toString());

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
            category.setText(question.getCategory().get_name());

            //TODO sassurer que toutes les categories aient des images puis enlever le if?
            if(getDrawable(question.getCategory().get_imageId()) != null) {
                icon.setBackground(getDrawable(question.getCategory().get_imageId()));
            };
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

                    v.setBackgroundColor(Color.GREEN);
                    UserProfile.getUserProfile("1").addCorrectAnswer();
//                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }else {
                    Log.i("activity.question", "answer is incorrect");
                    v.setBackgroundColor(Color.RED);
//                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    UserProfile.getUserProfile("1").addIncorrectAnswer();
                }
                newQuestion();
            }
        };
    }

    private void newQuestion(){
        questionCount++;
        QuestionFetcher questionFetcher = new QuestionFetcher();
        questionFetcher.execute("");
    }


    //reinitialise laffichage
    private void reinitializer(){
        addTimeButton.setClickable(true);
        UserProfile userProfile = UserProfile.getUserProfile("1");
        correctlyAnswered.setText(String.valueOf(userProfile.getCorrectlyAnswered()));
        pointsEarned.setText(String.valueOf(userProfile.getPointsEarned()));
        if (countDownTimer != null) {
            countDownTimer.cancel();}
        countDownTimer = new MyCountDownTimer(baseTime, 50);
        countDownTimer.start();

    }

    //custom countdownTimer class pour la progress bar et le temps pour repondre
    private class MyCountDownTimer extends CountDownTimer {
        private long timeRemaining;

        private MyCountDownTimer(long startTime, long timeBetweenTicks) {
            super(startTime, timeBetweenTicks);
        }

        @Override
        @TargetApi(21)
        public void onTick(long millisUntilFinished) {

            timeRemaining = millisUntilFinished;

            //change la couleur en fonction du temps restant
            if(millisUntilFinished <= 1000) {
                pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
            } else if (millisUntilFinished <= 3000){
                int colorTransition = (int) (millisUntilFinished - 1000) * 255 /2000;
                pb.setProgressTintList(ColorStateList.valueOf(0xFFFF0000 + (colorTransition << 8)));
            } else if (millisUntilFinished <= 5000){
                int colorTransition = (int) (millisUntilFinished - 3000) * 255 /2000;
                pb.setProgressTintList(ColorStateList.valueOf(0xFFFFFF00 - (colorTransition << 16)));
            } else {
                pb.setProgressTintList(ColorStateList.valueOf(0xFF00FF00));
            }
            pb.setProgress((int) millisUntilFinished / (baseTime/100));
        }

        @Override
        public void onFinish() {
            newQuestion();
            //TODO que faire dautre lorsquil ne reste plus de temps
        }
        public long getTimeRemaining() {
            return timeRemaining;
        }
    }

    //ce quil se passe lorsque lon clique sur +5 sec
    public void addTime(View v) {
        addTimeButton.setClickable(false);
        long newTime = countDownTimer.getTimeRemaining() + 5000;
        countDownTimer.cancel();
        countDownTimer = new MyCountDownTimer(newTime, 50);
        countDownTimer.start();
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
