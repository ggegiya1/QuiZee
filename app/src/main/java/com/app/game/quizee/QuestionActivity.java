package com.app.game.quizee;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ggegiya1 on 2/16/17.
 */

public class QuestionActivity extends AppCompatActivity{

    //User interface attributes
    TextSwitcher questionTextSwitcher;

    TextSwitcher answer1TextSwitcher;
    TextSwitcher answer2TextSwitcher;
    TextSwitcher answer3TextSwitcher;
    TextSwitcher answer4TextSwitcher;
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
    String mode;
    ProgressBar pb;
    MyCountDownTimer countDownTimer;
    List<TextSwitcher> TextSwitchers;
    TextView category;
    ImageView icon;

    //Categories Game mode Attributes
    Boolean Computers;
    Boolean History;
    Boolean Music;
    Boolean VideoGames;
    Boolean Art;
    Boolean GeneralKnowledge;
    Boolean Geography;
    Boolean categories[];
    Integer trueCategories;

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
        questionTextSwitcher = (TextSwitcher) findViewById(R.id.text_question);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setRotation(180);
        //ajoute les view a créé lors de lanimation de changement de texte du questionTextSwitcher
        //viewfactory tiré de la page http://www.androhub.com/android-textswitcher/
        questionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                //crer un TextView avec des caractéristiques
                TextView myText = new TextView(QuestionActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(30);
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
                            return myButton;
                        }
                    });
        }

        //timer = new Timer(true); TODO effacer si inutile
        newQuestion();


        /* ajout de tristan TODO effacer si inutile
        mode = getIntent().getStringExtra("mode");
        if (mode.equals("categoriesPlay")) {
            Computers = getIntent().getBooleanExtra("Computers" , true);
            History = getIntent().getBooleanExtra("History" , true);
            Music = getIntent().getBooleanExtra("Music" , true);
            VideoGames = getIntent().getBooleanExtra("VideoGames" , true);
            Art = getIntent().getBooleanExtra("Art" , true);
            GeneralKnowledge = getIntent().getBooleanExtra("GeneralKnowledge" , true);
            Geography = getIntent().getBooleanExtra("Geography" , true);
            categories = new Boolean[7];
            trueCategories = 0;
            for(int i = 0; i < categories.length; i++) {
                trueCategories ++;
            }
        }*/
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
            Log.i("activity.question", "fetched question: " + question.toString());
            questionTextSwitcher.setText(question.getText_question());
            List<String> answers = question.getAnswers(true);

            //efface les couleurs sur les boutons et mets le texte correspondant aux boutons
            for (int i=0; i<TextSwitchers.size(); i++){
                TextSwitcher ts = TextSwitchers.get(i);
                ts.setText(answers.get(i));
                ts.getChildAt(0).getBackground().clearColorFilter();
                ts.getChildAt(1).getBackground().clearColorFilter();
                category.setText(question.getCategory().get_name());
                switch (question.getCategory().get_name()) {
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
        /*if(mode.equals("categoriesPlay")) { //ajout de tristan
            QuestionFetcher questionFetcher = new QuestionFetcher();
            questionFetcher.execute("");
        }
        if(mode.equals("quickPlay")) {*/
        QuestionFetcher questionFetcher = new QuestionFetcher();
        questionFetcher.execute("");
        // }
    }

    private void reinitializer(){
        UserProfile userProfile = UserProfile.getUserProfile("1");
        correctlyAnswered.setText(String.valueOf(userProfile.getCorrectlyAnswered()));
        pointsEarned.setText(String.valueOf(userProfile.getPointsEarned()));
        if (countDownTimer != null) {
            countDownTimer.cancel();}
        countDownTimer = new MyCountDownTimer(5000, 50);
        countDownTimer.start();
    }

    //custom countdownTimer class
    private class MyCountDownTimer extends CountDownTimer {
        private long timeRemaining;

        private MyCountDownTimer(long startTime, long timeBetweenTicks) {
            super(startTime, timeBetweenTicks);
        }

        @Override
        @TargetApi(21)
        public void onTick(long millisUntilFinished) {
            timeRemaining = millisUntilFinished;
            if(millisUntilFinished <= 1000) {
                pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
            } else if (millisUntilFinished <= 3000){
                int colorTransition = (int) (millisUntilFinished - 1000) * 255 /2000;
                pb.setProgressTintList(ColorStateList.valueOf(0xFFFF0000 + (colorTransition << 8)));
                Log.i("couleur", "" + colorTransition);
            } else if (millisUntilFinished <= 5000){
                int colorTransition = (int) (millisUntilFinished - 3000) * 255 /2000;
                pb.setProgressTintList(ColorStateList.valueOf(0xFFFFFF00 - (colorTransition << 16)));
            } else {
                pb.setProgressTintList(ColorStateList.valueOf(0xFF00FF00));
            }
            pb.setProgress((int) millisUntilFinished / 50);
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
        Log.i("activity question", "user left question activity");

        Ad.setNegativeButton(R.string.no, null);
        Ad.show();
    }
}
