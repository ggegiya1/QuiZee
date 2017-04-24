package com.app.game.quizee;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiplayerQuestionActivity extends AppCompatActivity{

    private final TriviaApi triviaApi = new TriviaApi(Collections.singletonList(Category.any()), 10, true);

    private Player player;

    //User interface attributes
    TextSwitcher questionTextSwitcher;
    TextSwitcher answer1TextSwitcher;
    TextSwitcher answer2TextSwitcher;
    TextSwitcher answer3TextSwitcher;
    TextSwitcher answer4TextSwitcher;

    Button correctlyAnswered;
    //Button pointsEarned; TODO remove if unused


    ProgressBar pb;
    MyCountDownTimer countDownTimer;
    List<TextSwitcher> TextSwitchers;
    TextView category;
    ImageView icon;
    int questionCount;
    Question currentQuestion;

    int baseTime = 10000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_question);

        questionTextSwitcher = (TextSwitcher) findViewById(R.id.text_question);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setRotation(180);
        questionCount = -1;

        int id_player1 = getIntent().getIntExtra("ID_P1",0);
        int id_player2 = getIntent().getIntExtra("ID_P2",0);

        //ajoute les view a créé lors de lanimation de changement de texte du questionTextSwitcher
        //viewfactory tiré de la page http://www.androhub.com/android-textswitcher/
        questionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                //crer un TextView avec des caractéristiques
                TextView myText = new TextView(MultiplayerQuestionActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                myText.setBackground(getResources().getDrawable((R.drawable.button_secondary_default)));
                return myText;
            }
        });

        category = (TextView) findViewById(R.id.caterogy_Textview);

        icon = (ImageView) findViewById(R.id.caterogy_Icon);
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
                            Button myButton = (Button)getLayoutInflater().inflate(R.layout.answer_button_template, null);
                            myButton.setOnClickListener(answerValidator());
                            return myButton;
                        }
                    });
        }
        newQuestion();

    }

    private class QuestionFetcher extends AsyncTask<Category, Object, Question>{
        @Override
        protected Question doInBackground(Category... params) {
            Question question = triviaApi.getQuestion();
            return question;
        }

        @Override
        protected void onPostExecute(Question question) {
            Log.i("activity.question", "fetched question: " + question.toString());
            currentQuestion = question;
            //change le texte de la question
            questionTextSwitcher.setText(question.getTextQuestion());

            //ajuste le taille du texte pour que le texte ne depasse pas
            TextView tv1 = (TextView) questionTextSwitcher.getChildAt(0);
            TextView tv2 = (TextView) questionTextSwitcher.getChildAt(1);
            if(questionCount%2 == 1) {
                tv1.setTextSize(40 - question.getTextQuestion().length() / 6);
            } else {
                tv2.setTextSize(40 - question.getTextQuestion().length() / 6);
            }

            List<Answer> answers = question.getAnswers(true);

            //efface les couleurs sur les boutons et mets le texte correspondant aux boutons
            for (int i=0; i<TextSwitchers.size(); i++){
                TextSwitcher ts = TextSwitchers.get(i);
                ts.setText(answers.get(i).getText());
                if(questionCount % 2 == 0) {
                    ts.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
                }
                else {
                    ts.getChildAt(1).setBackground(getResources().getDrawable(R.drawable.button_tertiary_default));
                }

                //met un icone correspondant a la category TODO aller cherche licone programaticallement
                category.setText(question.getCategory().getName());
                switch (question.getCategory().getName()) {
                    case "General Knowledge" : icon.setBackgroundResource(R.drawable.ic_general_knowledge);
                        break;
                    case "Science: Computers" : icon.setBackgroundResource(R.drawable.ic_computer);
                        break;
                    case "Geography" : icon.setBackgroundResource(R.drawable.ic_geography);
                        break;
                    case "Art" : icon.setBackgroundResource(R.drawable.ic_art);
                        break;
                    case "History" : icon.setBackgroundResource(R.drawable.ic_history);
                        break;
                    case "Entertainment: Music" : icon.setBackgroundResource(R.drawable.music_category_icon);
                        break;
                    case "Entertainment: Video Games" : icon.setBackgroundResource(R.drawable.videogames_category_icon);
                        break;
                }
            }
            reinitializer();
        }
    }

    private View.OnClickListener answerValidator(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer answer = (Answer)v.getTag();
                Log.i("activity.question", String.format("answer: %s", answer));
                if (answer.isCorrect()){
                    Log.i("activity.question", "answer is correct");

                    v.setBackgroundColor(Color.GREEN);
                    player.addCorrectAnswer(currentQuestion);
                }else {
                    Log.i("activity.question", "answer is incorrect");
                    v.setBackgroundColor(Color.RED);
                    player.addIncorrectAnswer(currentQuestion);
                }
                newQuestion();
            }
        };
    }

    private void newQuestion(){
        questionCount++;
        QuestionFetcher questionFetcher = new QuestionFetcher();
        questionFetcher.execute(Category.any());
    }

    private void reinitializer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();}
        countDownTimer = new MyCountDownTimer(baseTime, 50);
        countDownTimer.start();

    }

    //custom countdownTimer class
    private class MyCountDownTimer extends CountDownTimer {
        private long timeRemaining;

        private MyCountDownTimer(long startTime, long timeBetweenTicks) {
            super(startTime, timeBetweenTicks);
        }

        @Override
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
        Log.i("activity question", "user left question activity");

        Ad.setNegativeButton(R.string.no, null);
        Ad.show();
    }
}
