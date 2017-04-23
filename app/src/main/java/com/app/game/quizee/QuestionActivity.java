package com.app.game.quizee;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Game;
import com.app.game.quizee.backend.GameManager;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.Question;
import com.app.game.quizee.util.AutofitTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements Game, Observer{

    private TriviaApi triviaApi;

    // TODO pass player as parameter on start
    private Player player;
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

    //answer buttons
    TextView correctlyAnswered;
    TextView timerView;
    TextView questionsContView;

    //power ups buttons
    Button skipButton;
    Button addTimeButton;
    Button bombButton;
    Button hintButton;

    // player stats
    TextView pointsView;
    TextView levelView;

    //game Attributes
    MyCountDownTimer countDownTimer;
    int questionCount;
    Question currentQuestion;

    //Total game scores
    int pscore=0;
    int totalscore=0;

    static final int BASE_TIME_MILLIS = 15000; // temps entre les questions en milisecondes
    static final int QUESTIONS_NUMBER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        player = (Player)bundle.getSerializable("player");
        player.addObserver(this);
        gameManager = new GameManager(this, player);

        pointsView = (TextView) findViewById(R.id.currency);
        levelView = (TextView) findViewById(R.id.level);

        // power-ups
        addTimeButton = (Button) findViewById(R.id.button_add_time);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.executeTime();
            }
        });
        skipButton = (Button) findViewById(R.id.button_question_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.executeSkip();
            }
        });
        bombButton = (Button) findViewById(R.id.button_bomb);
        bombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.executeBomb();
            }
        });
        hintButton = (Button) findViewById(R.id.button_hint);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.executeHint();
            }
        });

        questionTextView = (AutofitTextView) findViewById(R.id.text_question);

        categoryNameView = (TextView) findViewById(R.id.caterogy_Textview);

        categoryIcon = (ImageView) findViewById(R.id.caterogy_Icon);
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
        player.prepareForGame();
        questionCount = 0;
        triviaApi = new TriviaApi(player.getCategoriesSelected(), QUESTIONS_NUMBER, false);
        Log.i("question.activity", "starting game for player: " + player);
        updateScore(player);
        newQuestion();
    }


    private void updatePowerUpButtons(){
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
                if (answer.isCorrect()){
                    setpref_category(currentQuestion.getCategory());
                    player.addCorrectAnswer(currentQuestion);
                    onAnswerButtonEffect(v, Color.GREEN);

                }else {
                    player.addIncorrectAnswer(currentQuestion);
                    onAnswerButtonEffect(v, Color.RED);
                }
                correctlyAnswered.setText(String.valueOf(player.getCorrectlyAnswered()));
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
        if(questionCount >= QUESTIONS_NUMBER) {
            countDownTimer.cancel();
            endDialog(player);
        } else {
            updatePowerUpButtons();
            QuestionFetcher questionFetcher = new QuestionFetcher();
            questionFetcher.execute();
        }
    }

    //crée le dialog de fin de jeu et laffiche
    private void endDialog(Player player) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.single_play_game_end,null);
        builder.setView(dialogView);
        final AlertDialog endDialog = builder.create();

        //felicitations
        pscore = player.getCorrectlyAnswered();
        totalscore = player.getScore();
        setpref_highscore();
        TextView felicitations = (TextView) dialogView.findViewById(R.id.end_felicitations);
        String fel[] = getResources().getStringArray(R.array.game_end_felicitation);
        felicitations.setText(fel[pscore]);

        TextView goodAnswersTv = (TextView) dialogView.findViewById(R.id.end_good_answers);
        //Mettre le score total aussi?
        goodAnswersTv.setText(getString(R.string.goodAnswers) + ": " + pscore);

        ListView achievementsEarned = (ListView) dialogView.findViewById(R.id.end_achievements_earned);

        //TODO get achievements earned programmatically
        ArrayList<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 5 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 1 question", 10, 10, 5, 10));

        AchievementsAdapter adapter = new AchievementsAdapter(this,  achievements);
        achievementsEarned.setAdapter(adapter);

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
                finish();
            }
        });
        //TODO reparer le bug qui fait que ca part nimporte quand
        endDialog.show();
        endDialog.setCancelable(false);
    }

    //Fonction pour write les préférences
    private void setpref_highscore(){
        SharedPreferences preferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //overwrite
        if (check_highscore(preferences)==true){
            editor.putInt("SCORE:", totalscore);
            editor.commit();
        }
    }

    private void setpref_category(Category mycat){
            String temp = String.valueOf(mycat.getId());
            SharedPreferences preferences = getSharedPreferences(temp, Context.MODE_PRIVATE);
            int nbQCategory = preferences.getInt(mycat.getName(), 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(temp, nbQCategory+=1);
            editor.commit();
    }

    private boolean check_highscore(SharedPreferences p1){
        //clé,default value
        if (p1.getInt("SCORE:",0)<totalscore){
            //We have a highscore!
            return true;
        }
        return false;
    }

    //Exemple de fonction pour lire les préférences
    private void get_pref_highscore(){
        SharedPreferences preferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        //Fetch la valeur à la clé SCORE, sinon met par défaut
        String highscore = preferences.getString ("SCORE:","No scores yet!");
    }

    private int get_pref_category(Category mycat){
        SharedPreferences preferences = getSharedPreferences(String.valueOf(mycat.getId()), Context.MODE_PRIVATE);
        //Fetch la valeur à la clé SCORE, sinon met par défaut
        return preferences.getInt(mycat.getName(),0);
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

        //met un icone et un texte correspondant a la category
        categoryNameView.setText(question.getCategory().getDisplayName());
        categoryIcon.setImageResource(question.getCategory().getImageId());

        questionsContView.setText(String.format("%s/%s", questionCount, QUESTIONS_NUMBER));

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
                //TODO: Fonction ne randomize pas, donne toujours la bonne réponse
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
        levelView.setText(String.valueOf(player.getLevel()));
        pointsView.setText(String.valueOf(player.getPoints()));
    }
}
