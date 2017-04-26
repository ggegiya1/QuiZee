package com.app.game.quizee;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Game;
import com.app.game.quizee.backend.GameManager;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.backend.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import me.grantland.widget.AutofitTextView;

public class QuestionActivity extends AppCompatActivity implements Game, Observer{

    private TriviaApi triviaApi;

    // TODO pass player as parameter on start
    private GameManager gameManager;

    //User interface attributes
    TextView questionTextView;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
    Button answer4Button;

    List<Button> answerButtons;

    TextView categoryNameView;
    ImageView categoryIcon;
    TextView difficulty;

    //player stats
    TextView scoreView;
    TextView pointsView;

    //answer buttons
    TextView correctlyAnswered;
    TextView timerView;
    TextView questionsContView;

    //power ups buttons
    Button skipButton;
    Button addTimeButton;
    Button bombButton;
    Button hintButton;


    //game Attributes
    MyCountDownTimer countDownTimer;
    int questionCount;
    Question currentQuestion;

    //Total game scores
    int pscore=0;

    boolean isPracticeMode;

    SharedPreferences prefs;
    boolean colorBlind;

    static final int BASE_TIME_MILLIS = 15000; // temps entre les questions en milisecondes
    static final int QUESTIONS_NUMBER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        isPracticeMode = getIntent().getBooleanExtra("isPracticeMode", false);
        Player player = getCurrentPlayer();
        player.addObserver(this);
        gameManager = new GameManager(this, player);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        colorBlind = prefs.getBoolean("colorblind_mode", false);

        // power-ups
        addTimeButton = (Button) findViewById(R.id.button_add_time);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameManager.executeTime()){
                    Toast.makeText(getApplicationContext(), "No more Time left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        skipButton = (Button) findViewById(R.id.button_question_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameManager.executeSkip()){
                    Toast.makeText(getApplicationContext(), "No more Skips left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bombButton = (Button) findViewById(R.id.button_bomb);
        bombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameManager.executeBomb()){
                    Toast.makeText(getApplicationContext(), "No more Bombs left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hintButton = (Button) findViewById(R.id.button_hint);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameManager.executeHint()){
                    Toast.makeText(getApplicationContext(), "No more Hints left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });

        questionTextView = (AutofitTextView) findViewById(R.id.text_question);
        if (isPracticeMode){
            questionTextView.setBackground(getDrawable(R.drawable.practice));
        }

        categoryNameView = (TextView) findViewById(R.id.caterogy_name);
        categoryIcon = (ImageView) findViewById(R.id.caterogy_Icon);
        difficulty = (TextView) findViewById(R.id.question_difficulty);

        pointsView = (TextView) findViewById(R.id.points);
        scoreView = (TextView) findViewById(R.id.score);

        correctlyAnswered = (TextView) findViewById(R.id.correct_answer_count);
        questionsContView = (TextView) findViewById(R.id.question_count);
        timerView = (TextView) findViewById(R.id.timer);

        answerButtons = new ArrayList<>();
        answer1Button = (Button) findViewById(R.id.button_response_1);
        answer1Button.setOnClickListener(answerValidator());
        answerButtons.add(answer1Button);
        answer2Button = (Button) findViewById(R.id.button_response_2);
        answer2Button.setOnClickListener(answerValidator());
        answerButtons.add(answer2Button);
        answer3Button = (Button) findViewById(R.id.button_response_3);
        answer3Button.setOnClickListener(answerValidator());
        answerButtons.add(answer3Button);
        answer4Button = (Button) findViewById(R.id.button_response_4);
        answer4Button.setOnClickListener(answerValidator());
        answerButtons.add(answer4Button);

        init();
    }

    public void init() {
        Player player = getCurrentPlayer();
        player.onGameStart();
        questionCount = 0;
        triviaApi = getTriviaApi();
        Log.i("question.activity", "starting game for player: " + player);
        updateScore(player);
        newQuestion();
    }

    private TriviaApi getTriviaApi(){
        return isPracticeMode?
                new TriviaApi(Collections.singletonList(Category.any()), QUESTIONS_NUMBER, true):
                new TriviaApi(getCurrentPlayer().getCategoriesSelected(), QUESTIONS_NUMBER, false);
    }

    private void updatePowerUpButtons(){
        Player player = getCurrentPlayer();
        addTimeButton.setText(String.valueOf(player.getAddTimes().size()));
        hintButton.setText(String.valueOf(player.getHints().size()));
        bombButton.setText(String.valueOf(player.getBombs().size()));
        skipButton.setText(String.valueOf(player.getSkips().size()));
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
                // store remained time to use in the score calculation
                currentQuestion.setTimeRemained(countDownTimer.getTimeRemaining());
                v.clearAnimation();
                Answer answer = (Answer)v.getTag();
                Player player = getCurrentPlayer();
                if (answer.isCorrect()){
                    player.addCorrectAnswer(currentQuestion);
                    if(colorBlind) {
                        onAnswerButtonEffect(v, Color.WHITE);
                    } else {
                        onAnswerButtonEffect(v, Color.GREEN);
                    }


                }else {
                    player.addIncorrectAnswer(currentQuestion);
                    if(colorBlind) {
                        onAnswerButtonEffect(v, Color.BLACK);
                    } else {
                        onAnswerButtonEffect(v, Color.RED);
                    }
                }
                correctlyAnswered.setText(String.valueOf(player.getCorrectlyAnswered().size()));
            }
        };
    }

    private void onAnswerButtonEffect(View button, int color){
        button.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(0);
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                // load new question only when animation finished
                newQuestion();
            }
        });
        button.startAnimation(animation);
    }

    private void changeTextAnimation(final TextView view, final String text){
        final Animation animation = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(0);
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){
                // set question
                view.setText(text);
                view.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                view.setClickable(true);
            }
        });
        view.startAnimation(animation);
    }


    public void newQuestion(){
        if(questionCount >= QUESTIONS_NUMBER && !isPracticeMode) {
            countDownTimer.cancel();
            endDialog();
        } else {
            updatePowerUpButtons();
            QuestionFetcher questionFetcher = new QuestionFetcher();
            questionFetcher.execute();
        }
    }

    //cré le dialog de fin de jeu et laffiche
    private void endDialog() {
        final Player player = getCurrentPlayer();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.single_play_game_end,null);
        builder.setView(dialogView);
        final AlertDialog endDialog = builder.create();

        //felicitations
        pscore = player.getCorrectlyAnswered().size();
        TextView felicitations = (TextView) dialogView.findViewById(R.id.end_felicitations);
        String fel[] = getResources().getStringArray(R.array.game_end_felicitation);
        felicitations.setText(fel[pscore]);

        TextView goodAnswersTv = (TextView) dialogView.findViewById(R.id.end_good_answers);

        goodAnswersTv.setText(getString(R.string.goodAnswers) + ": " + pscore + " | Score: " + player.getCurrentScore());

        ListView achievementsEarned = (ListView) dialogView.findViewById(R.id.end_achievements_earned);

        updateAchievements();
        // IMPORTANT! Save the current player score to be updated in TOP list view
        PlayerManager.getInstance().saveCurrentPlayer();
        List<Achievement> achievements = updateAchievements();
        if (!achievements.isEmpty()){
            AchievementsAdapter adapter = new AchievementsAdapter(this,  updateAchievements());
            achievementsEarned.setAdapter(adapter);
        }

        Button replay = (Button) dialogView.findViewById(R.id.end_play_again_button_yes);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDialog.cancel();
                init();
            }
        });

        Button dontReplay = (Button) dialogView.findViewById(R.id.end_play_again_button_no);
        dontReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDialog.cancel();
                player.onGameEnd();
                finish();
            }
        });
        //TODO reparer le bug qui fait que ca part nimporte quand
        endDialog.show();
        endDialog.setCancelable(false);
    }

    private ArrayList<Achievement> updateAchievements(){
        ArrayList<Achievement> achievements = new ArrayList<>();
        Player player = getCurrentPlayer();
        for (Achievement a: Achievement.values()){
            if (a.isAchieved(player)){
                player.addAchievement(a);
                achievements.add(a);
            }
        }
        return achievements;

    }
    private void setQuestion(Question question){
        questionCount++;
        currentQuestion = question;
        Log.d("question", question.toString());
        //change le texte de la question
        changeTextAnimation(questionTextView, question.getTextQuestion());

        List<Answer> answers = question.getAnswers(true);
        for (int i=0; i<answers.size(); i++){
            Button button = this.answerButtons.get(i);
            Answer answer = answers.get(i);
            button.clearAnimation();
            button.setTag(answer);
            // restore the button colors and view
            button.getBackground().clearColorFilter();
            changeTextAnimation(button, answer.getText());
        }

        categoryNameView.setText(question.getCategory().getDisplayName());
        categoryIcon.setImageResource(question.getCategory().getImageId());
        difficulty.setText(question.getDifficulty().name());


        if (isPracticeMode){
            questionsContView.setText(String.valueOf(questionCount));
        }else {
            questionsContView.setText(String.format("%s/%s", questionCount, QUESTIONS_NUMBER));
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();}
        countDownTimer = new MyCountDownTimer(BASE_TIME_MILLIS, 100);
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
            timerView.setTextColor(getTimerColor(millisUntilFinished));
            timerView.setText(String.format(Locale.ROOT, "%.2f", (float) millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            // player did not respond
            getCurrentPlayer().addIncorrectAnswer(currentQuestion);
            newQuestion();
            //TODO que faire dautre lorsquil ne reste plus de temps
        }

        public long getTimeRemaining() {
            return timeRemaining;
        }

        private int getTimerColor(long millisUntilFinished) {
            float factor = ((float) millisUntilFinished / BASE_TIME_MILLIS);
            if (factor <= 0.5) {
                // from yellow to red
                return Color.rgb(255, (int) Math.round(255 * (factor / 0.5)), 0);

            } else {
                // from green to yellow
                return Color.rgb(255 - (int) Math.round(255 * (factor - 0.5) / 0.5), 255, 0);
            }
        }
    }

    @Override
    public void addTime() {
        updatePowerUpButtons();
        long newTime = countDownTimer.getTimeRemaining() + 5000;
        countDownTimer.cancel();
        countDownTimer = new MyCountDownTimer(newTime, 50);
        countDownTimer.start();
    }


    @Override
    public void deleteTwoIncorrectAnswers() {
        updatePowerUpButtons();
        int marked = 0;
        for(Button button: answerButtons){
            if (!((Answer)button.getTag()).isCorrect() && marked<2){
                button.getBackground().setColorFilter(Color.DKGRAY,  PorterDuff.Mode.SRC_ATOP);
                // prevent accidental click
                button.setClickable(false);
                marked++;
            }
        }
    }

    @Override
    public void showHint() {
        updatePowerUpButtons();
        boolean marked = false;
        int color = getResources().getColor(R.color.green);
        Random random = new Random(System.nanoTime());
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        for(Button button: answerButtons){
            if (((Answer)button.getTag()).isCorrect() && random.nextBoolean()){
                button.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                button.startAnimation(animation);
                marked = true;
                break;
            }
        }
        if (! marked){
            Button randomButton = answerButtons.get(random.nextInt(4));
            randomButton.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            randomButton.startAnimation(animation);
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
                getCurrentPlayer().onGameEnd();
                finish();
            }
        } );
        Ad.setNegativeButton(R.string.no, null);
        Ad.show();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Player){
            updateScore((Player) observable);
        }
    }

    private void updateScore(Player player) {
        scoreView.setText(String.format(Locale.ROOT, getResources().getString(R.string.score_format), player.getCurrentScore()));
        pointsView.setText(String.valueOf(player.getPoints()));
    }

    private Player getCurrentPlayer(){
        return isPracticeMode?
                Player.defaultPlayer():
                PlayerManager.getInstance().getCurrentPlayer();
    }
}
