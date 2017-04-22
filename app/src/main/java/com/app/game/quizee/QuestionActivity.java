package com.app.game.quizee;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Game;
import com.app.game.quizee.backend.GameManager;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.Question;
import com.app.game.quizee.util.AutoResizeTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements Game{

    private TriviaApi triviaApi;

    // TODO pass player as parameter on start
    private Player player;
    private GameManager gameManager;

    //User interface attributes
    AutoResizeTextView questionText;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
    Button answer4Button;

    List<Button> answerButtons;

    TextView category;
    ImageView icon;

    //answer buttons
    TextView correctlyAnswered;
    TextView timer;
    TextView questionsLeft;

    //power ups buttons
    Button skipButton;
    Button addTimeButton;
    Button bombButton;
    Button hintButton;

    //game Attributes
    MyCountDownTimer countDownTimer;
    int questionCount;
    Question currentQuestion;

    static final int BASE_TIME_MILLIS = 15000; // temps entre les questions en milisecondes
    static final int QUESTIONS_NUMBER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        player = (Player)bundle.getSerializable("player");
        gameManager = new GameManager(this, player);
        Log.i("question.activity", "starting game for player: " + player);
        triviaApi = new TriviaApi(player.getCategoriesSelected(), QUESTIONS_NUMBER, false);

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

        questionText = (AutoResizeTextView) findViewById(R.id.text_question);
        questionText.setMinTextSize(10);

        category = (TextView) findViewById(R.id.caterogy_Textview);

        icon = (ImageView) findViewById(R.id.caterogy_Icon);
        correctlyAnswered = (TextView) findViewById(R.id.correct_answer_count);
        questionsLeft = (TextView) findViewById(R.id.question_count);
        timer = (TextView) findViewById(R.id.timer);

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
                Answer answer = (Answer)v.getTag();
                if (answer.isCorrect()){
                    buttonEffect(v, Color.GREEN);
                    player.addCorrectAnswer(currentQuestion);
                }else {
                    buttonEffect(v, Color.RED);
                    player.addIncorrectAnswer(currentQuestion);
                }
                newQuestion();
            }
        };
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

    //cré le dialog de fin de jeu et laffiche
    private void endDialog(Player player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = (View)  getLayoutInflater().inflate(R.layout.single_play_game_end,null);
        builder.setView(dialogView);
        final AlertDialog endDialog = builder.create();

        //felicitations
        TextView felicitations = (TextView) dialogView.findViewById(R.id.end_felicitations);
        String fel[] = getResources().getStringArray(R.array.game_end_felicitation);
        felicitations.setText(fel[player.getCorrectlyAnswered()]);

        TextView goodAnswersTv = (TextView) dialogView.findViewById(R.id.end_good_answers);
        goodAnswersTv.setText(getString(R.string.goodAnswers) + ": " + player.getCorrectlyAnswered());

        ListView achievementsEarned = (ListView) dialogView.findViewById(R.id.end_achievements_earned);

        //TODO get achievements earned programmatically
        ArrayList<Achievement> achievements = new ArrayList<Achievement>();
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));
        achievements.add(new Achievement(0, "Answer 10 questions", 10, 10, 5, 10));

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
        //endDialog.show();
        endDialog.setCancelable(false);
    }


    private void setQuestion(Question question){
        questionCount++;
        currentQuestion = question;
        //change le texte de la question
        questionText.setText(question.getText_question());

        List<Answer> answers = question.getAnswers(true);
        for (int i=0; i<answers.size(); i++){
            this.answerButtons.get(i).setText(answers.get(i).getText());
            this.answerButtons.get(i).setTag(answers.get(i));
            this.answerButtons.get(i).setBackground(getResources().getDrawable(R.color.answerButton));
            this.answerButtons.get(i).setClickable(true);
        }

        //met un icone et un texte correspondant a la category
        category.setText(question.getCategory().getDisplayName());
        icon.setImageResource(question.getCategory().getImageId());
        addTimeButton.setClickable(true);
        questionsLeft.setText(String.format("%s/%s", questionCount, QUESTIONS_NUMBER));
        correctlyAnswered.setText(String.valueOf(player.getCorrectlyAnswered()));
//        pointsEarned.setText(String.valueOf(player.getPointsEarned()));
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
            timer.setTextColor(getTimerColor(millisUntilFinished));
            timer.setText(String.format(Locale.ROOT, "%.2f", (float) millisUntilFinished / 1000));
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
        addTimeButton.setClickable(false);
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
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setClickable(false);
                marked++;
            }
        }
    }

    @Override
    public void showHint() {
        updatePowerUpButtons();
        boolean marked = false;
        Drawable color = getResources().getDrawable(R.color.colorAccent);
        Random random = new Random(System.nanoTime());
        for(Button button: answerButtons){
            if (((Answer)button.getTag()).isCorrect() && random.nextBoolean()){
                button.setBackground(color);
                marked = true;
                break;
            }
        }
        if (! marked){
            answerButtons.get(random.nextInt(4)).setBackground(color);
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
