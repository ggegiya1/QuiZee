package com.app.game.quizee.layout;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.Game;
import com.app.game.quizee.backend.MusicService;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.backend.PowerUp;
import com.app.game.quizee.backend.Question;
import com.app.game.quizee.backend.TriviaApi;

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

    boolean isPracticeMode;

    SharedPreferences prefs;
    boolean gameSounds;
    EndDialog endDialog;

    static final int BASE_TIME_MILLIS = 15000; // temps entre les questions en milisecondes
    static final int QUESTIONS_NUMBER = 10; // max questions per single game

    /**
     * Create and initialize the main view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        isPracticeMode = getIntent().getBooleanExtra("isPracticeMode", false);
        final Player player = getCurrentPlayer();
        player.addObserver(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        gameSounds = prefs.getBoolean("sound_game", false);

        addPowerUpButtons();
        addToolBar();
        addAnswerButtons();
        questionTextView = (AutofitTextView) findViewById(R.id.text_question);
        questionsContView = (TextView) findViewById(R.id.question_count);
        timerView = (TextView) findViewById(R.id.timer);

        // disable score in practice mode
        if (isPracticeMode){
            hideScoreElements();
        }
        endDialog = new EndDialog(this);
        init();
    }

    private void hideScoreElements(){
        pointsView.setVisibility(View.INVISIBLE);
        scoreView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void init() {
        Player player = getCurrentPlayer();
        player.onGameReset();
        questionCount = 0;
        triviaApi = getTriviaApi();
        Log.i("question.activity", "starting game for player: " + player);
        updateScore(player);
        newQuestion();
    }


    private void addPowerUpButtons(){
        final Player player = getCurrentPlayer();
        // power-ups
        addTimeButton = (Button) findViewById(R.id.button_add_time);
        addTimeButton.setBackgroundResource(PowerUp.ADDTIME.getColorRessouce());
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tryPowerUp(player, PowerUp.ADDTIME)){
                    Toast.makeText(getApplicationContext(), "No more Time left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        skipButton = (Button) findViewById(R.id.button_question_skip);
        skipButton.setBackgroundResource(PowerUp.SKIP.getColorRessouce());
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tryPowerUp(player, PowerUp.SKIP)){
                    Toast.makeText(getApplicationContext(), "No more Skips left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bombButton = (Button) findViewById(R.id.button_bomb);
        bombButton.setBackgroundResource(PowerUp.BOMB.getColorRessouce());
        bombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tryPowerUp(player, PowerUp.BOMB)){
                    Toast.makeText(getApplicationContext(), "No more Bombs left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hintButton = (Button) findViewById(R.id.button_hint);
        hintButton.setBackgroundResource(PowerUp.HINT.getColorRessouce());
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tryPowerUp(player, PowerUp.HINT)){
                    Toast.makeText(getApplicationContext(), "No more Hints left\nYou can buy one in store", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addAnswerButtons(){
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
    }

    private void addToolBar(){
        categoryNameView = (TextView) findViewById(R.id.caterogy_name);
        categoryIcon = (ImageView) findViewById(R.id.caterogy_Icon);
        difficulty = (TextView) findViewById(R.id.question_difficulty);
        pointsView = (TextView) findViewById(R.id.points);
        scoreView = (TextView) findViewById(R.id.score);
    }

    private TriviaApi getTriviaApi(){
        return isPracticeMode?
                new TriviaApi(Collections.singletonList(Category.any()), QUESTIONS_NUMBER, true):
                new TriviaApi(getCurrentPlayer().getCategoriesSelected(), QUESTIONS_NUMBER, true);
    }

    private void updatePowerUpButtons(){
        Player player = getCurrentPlayer();
        addTimeButton.setText(String.valueOf(player.getNumberAvailablePowerUps(PowerUp.ADDTIME)));
        hintButton.setText(String.valueOf(player.getNumberAvailablePowerUps(PowerUp.HINT)));
        bombButton.setText(String.valueOf(player.getNumberAvailablePowerUps(PowerUp.BOMB)));
        skipButton.setText(String.valueOf(player.getNumberAvailablePowerUps(PowerUp.SKIP)));
    }

    private boolean tryPowerUp(Player player, PowerUp powerUp){
        int available = player.getNumberAvailablePowerUps(powerUp);
        if (available < 1){
            return false;
        }
        powerUp.apply(this);
        player.removePowerUp(powerUp);
        updatePowerUpButtons();
        return true;
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
                countDownTimer.cancel();
                v.clearAnimation();
                Answer answer = (Answer)v.getTag();
                Player player = getCurrentPlayer();
                MediaPlayer mp = new MediaPlayer();
                if (answer.isCorrect()){
                    // do not count correct answers in practice mode
                    if (!isPracticeMode) {
                        player.addCorrectAnswer(currentQuestion);
                    }
                    // animate correct answer
                    onAnswerButtonEffect(v, Color.GREEN);
                    if(gameSounds) {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.gooda);
                        mp.start();
                    }
                    resultAnimation(questionTextView, getString(R.string.correct_answer), Color.GREEN);
                }else {
                    // do not count incorrect answers in practice mode
                    if (!isPracticeMode) {
                        player.addIncorrectAnswer(currentQuestion);
                    }
                    // animate incorrect answer
                    if(gameSounds) {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.wronga);
                        mp.start();
                    }
                    onAnswerButtonEffect(v, Color.RED);
                    resultAnimation(questionTextView, getString(R.string.wrong_answer), Color.RED);
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            }
        };
    }


    /**
     * Add flashing animation to the specified button
     */
    private void onAnswerButtonEffect(View button, int color){
        button.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        setButtonsClickable(false);
        button.startAnimation(animation);
    }

    /**
     * Add Fade-In animation to the text view. Used when changing one text to another
     */
    private void fadeInText(final TextView view, String text){
        final Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(0);
        view.setText(text);
        view.startAnimation(animation);
    }

    /**
     * Show animation for correct / incorrect question
     * Launch the next question
     */
    private void resultAnimation(final TextView view, final String text, final int color){
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from invisible to fully visible
        animation.setDuration(1500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(0);
        final ColorStateList origColors = view.getTextColors();
        final float origSize = view.getTextSize();
        final Typeface styleOrig = view.getTypeface();
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){
                // set question
                view.setTextColor(color);
                view.setTypeface(null, Typeface.BOLD);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
                view.setText(text);
            }

            @Override
            public void onAnimationRepeat(Animation animation){
            }

            @Override
            public void onAnimationEnd(Animation animation){
                // restore previous text attributes
                view.setTextColor(origColors);
                view.setTextSize(origSize);
                view.setTypeface(styleOrig);
                // load new question only when animation finished
                newQuestion();
            }
        });
        view.startAnimation(animation);
    }

    /**
     * Fetch and show the new question
     * Validate if the max number of question have been played and the end of the game has been reached
     */
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


    /**
     * Create and show the end dialog with stats and achievements
     */
    private void endDialog() {
        final Player player = getCurrentPlayer();
        // play endgame sound
        if(gameSounds) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.endgame);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mmp) {
                    mmp.release();
                }
            });
        }
        // show end dialog
        endDialog.show(this, player);
    }

    /**
     * Update question text and answer buttons with the new question
     */
    private void setQuestion(Question question){
        questionCount++;
        currentQuestion = question;
        //change le texte de la question
        fadeInText(questionTextView, question.getTextQuestion());

        List<Answer> answers = question.getAnswers(true);
        for (int i=0; i<answers.size(); i++){
            Button button = this.answerButtons.get(i);
            Answer answer = answers.get(i);
            button.clearAnimation();
            button.setTag(answer);
            // restore the button colors and view
            button.getBackground().clearColorFilter();
            fadeInText(button, answer.getText());
        }

        categoryNameView.setText(question.getCategory().getDisplayName());
        categoryIcon.setImageResource(question.getCategory().getImageId());
        difficulty.setText(question.getDifficulty().name());
        difficulty.setTextColor(question.getDifficulty().getColor());


        if (isPracticeMode){
            // in the practice mode show only the number of question answered
            questionsContView.setText(String.valueOf(questionCount));
        }else {
            // in the practice mode show the ratio of answered / left
            questionsContView.setText(String.format("%s/%s", questionCount, QUESTIONS_NUMBER));
        }

        runCountDownTimer();
    }

    /**
     * Start countdown timer
     */
    private void runCountDownTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();}
        countDownTimer = new MyCountDownTimer(BASE_TIME_MILLIS, 100);
        countDownTimer.start();
        setButtonsClickable(true);
    }

    /**
     *  Custom countdownTimer class to get the remaining time to answer
     */
    private class MyCountDownTimer extends CountDownTimer {
        private long timeRemaining;

        private MyCountDownTimer(long startTime, long timeBetweenTicks) {
            super(startTime, timeBetweenTicks);
        }

        /**
         * Update the timer view on each tick
         * @param millisUntilFinished
         */
        @Override
        @TargetApi(21)
        public void onTick(long millisUntilFinished) {
            // update timer on tick
            timeRemaining = millisUntilFinished;
            timerView.setTextColor(getTimerColor(millisUntilFinished));
            timerView.setText(String.format(Locale.ROOT, "%.2f", (float) millisUntilFinished / 1000));
        }

        /**
         * Get remaining time to respond to the current question
         * @return remaining time in milliseconds
         */
        public long getTimeRemaining() {
            return timeRemaining;
        }

        @Override
        public void onFinish() {
            // player did not respond
            getCurrentPlayer().addIncorrectAnswer(currentQuestion);
            // show 0 sec left
            timerView.setText("0:00");
            // show time expired animated message
            resultAnimation(questionTextView, getString(R.string.time_expired), Color.RED);
        }

        /**
         * Calculate the timer color on progress from green to yellow and from yellow to red
         */
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

    /**
     * Toggle the buttons
     * The buttons should be disabled during the response validation and animations
     * @param b
     */
    public void setButtonsClickable(boolean b) {
        bombButton.setClickable(b);
        addTimeButton.setClickable(b);
        skipButton.setClickable(b);
        hintButton.setClickable(b);
        answer1Button.setClickable(b);
        answer2Button.setClickable(b);
        answer3Button.setClickable(b);
        answer4Button.setClickable(b);
    }

    /**
     * Executed by Skip PowerUp
     * Skip to the next question and don't count the current question
     */
    @Override
    public void skipQuestion() {
        questionCount--;
        resultAnimation(questionTextView, getString(R.string.skip_question), Color.GREEN);
    }

    /**
     * Executed by AddTime PowerUp
     * Adds 5 sec extra time to the game
     */
    @Override
    public void addTime() {
        long newTime = countDownTimer.getTimeRemaining() + 10000;
        countDownTimer.cancel();
        countDownTimer = new MyCountDownTimer(newTime, 50);
        countDownTimer.start();
        bombButton.setClickable(false);
    }

    /**
     * Executed by Bomb PowerUp
     * Disables (shadows) two incorrect answers
     */
    @Override
    public void deleteTwoIncorrectAnswers() {
        int marked = 0;
        for(Button button: answerButtons){
            if (!((Answer)button.getTag()).isCorrect() && marked<2){
                button.getBackground().setColorFilter(Color.LTGRAY,  PorterDuff.Mode.SRC_ATOP);
                // prevent accidental click
                button.setClickable(false);
                marked++;
            }
        }
    }

    /**
     * Executed by Hint PowerUp
     * Highlights an highly probable answer
     */
    @Override
    public void showHint() {
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        // Highlight the probable good answer
        Button probableAnswer = getProbableAnswer();
        probableAnswer.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        probableAnswer.startAnimation(animation);
    }

    /**
     * @return a highly probable answer
     */
    private Button getProbableAnswer(){
        Random random = new Random(System.nanoTime());
        for(Button button: answerButtons){
            if (((Answer)button.getTag()).isCorrect() && random.nextBoolean()){
                return button;
            }
        }
        return answerButtons.get(random.nextInt(4));
    }

    /**
     * Prevents quiting game accidentally on back pressed
     */
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
                getCurrentPlayer().onGameReset();
                finish();
            }
        } );
        Ad.setNegativeButton(R.string.no, null);
        Ad.show();
    }

    /**
     * Update the player's score once changed
     * @param observable Player
     * @param arg not used
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Player){
            updateScore((Player) observable);
            updatePowerUpButtons();
        }
    }


    private void updateScore(Player player) {
        scoreView.setText(String.valueOf(player.getCurrentScore()));
        pointsView.setText(String.valueOf(player.getPoints()));
    }

    /**
     * @return currently active player
     */
    private Player getCurrentPlayer(){
        return isPracticeMode?
                Player.defaultPlayer():
                PlayerManager.getInstance().getCurrentPlayer();
    }

    /**
     * used to pause music when Quizee goes on background
     */
    @Override
    protected void onPause() {
        MusicService.ServiceBinder.getService().pauseMusic();
        super.onPause();
    }

    /**
     * used to resume music when Quizee goes on background
     */
    @Override
    protected void onResume() {
        MusicService.ServiceBinder.getService().resumeMusic(true);
        super.onResume();
    }
}
