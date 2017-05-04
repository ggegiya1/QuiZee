package com.app.game.quizee.layout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.game.quizee.R;
import com.app.game.quizee.backend.Achievement;
import com.app.game.quizee.backend.Game;
import com.app.game.quizee.backend.Player;
import com.app.game.quizee.backend.PlayerManager;
import com.app.game.quizee.backend.Question;
import com.app.game.quizee.backend.Score;

import java.util.List;

/**
 * Created by gia on 03/05/17.
 */

public class EndDialog {

    private Game game;

    public EndDialog(Game game) {
        this.game = game;
    }

    /**
     * Create and show the end dialog with stats and achievements
     */
    public void show(Activity parentView, final Player player) {

        // calculate and show achievements
        AlertDialog.Builder builder = new AlertDialog.Builder(parentView);
        player.setNbGamesPlayed(player.getNbGamesPlayed()+1);
        View dialogView = LayoutInflater.from(parentView).inflate(R.layout.single_play_game_end,null);
        builder.setView(dialogView);
        final AlertDialog endDialog = builder.create();
        player.setTotalScore(player.getCurrentScore()+player.getTotalScore());
        player.setTotalratio(player.getTotalScore()/(player.getNbGamesPlayed()+1));

        //achievement title
        TextView achivement_status = (TextView) dialogView.findViewById(R.id.achievement_status);

        //felicitations
        int correctlyAnswered = player.getCorrectlyAnswered().size();
        TextView felicitations = (TextView) dialogView.findViewById(R.id.end_felicitations);
        String fel[] = parentView.getResources().getStringArray(R.array.game_end_felicitation);
        felicitations.setText(fel[correctlyAnswered]);

        TextView goodAnswersTv = (TextView) dialogView.findViewById(R.id.end_good_answers);

        populateScoreView(goodAnswersTv, player);
        // IMPORTANT! Save the current player score to be updated in TOP list view
        PlayerManager.getInstance().saveCurrentPlayer();
        List<Achievement> achievements = player.updateAchievements();
        if (!achievements.isEmpty()){
            ListView achievementsEarned = (ListView) dialogView.findViewById(R.id.end_achievements_earned);
            AchievementsAdapter adapter = new AchievementsAdapter(parentView,  achievements);
            achievementsEarned.setAdapter(adapter);
            achivement_status.setText(R.string.achivements_earned);
        }else{
            achivement_status.setText(R.string.no_achivement_earned);
        }

        Button replay = (Button) dialogView.findViewById(R.id.end_play_again_button_yes);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDialog.cancel();
                game.init();
            }
        });

        Button dontReplay = (Button) dialogView.findViewById(R.id.end_play_again_button_no);
        dontReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDialog.cancel();
                player.onGameReset();
                game.finish();
            }
        });
        endDialog.show();
        endDialog.setCancelable(false);
    }


    private void populateScoreView(TextView textView, Player player){
        Score score = calculateScore(player);
        String text = "Score: " + score.getTotalScore() + "\n" +
                "Good answers: " + player.getCorrectlyAnswered().size() + "\n";
        for (Question.Difficulty d: Question.Difficulty.values()){
            text += d.name() + ": " + score.getAnswers(d) + "(+" + score.getScore(d) + "pts)\n";
        }
        text += "Time bonus: " + (int)(score.getTimeLeft()/1000) + "sec (+" + score.getTimeBonus() + "pts)";
        textView.setText(text);
        player.addScore(score.getTotalScore());
        player.addmoney(score.getTotalScore());
        player.addexp(score.getTotalScore());
    }

    private Score calculateScore(Player player){
        Score score = new Score();
        long timeLeft = 0;
        for (Question q: player.getCorrectlyAnswered()){
            score.addAnswer(q.getDifficulty());
            timeLeft += q.getTimeRemained();
        }
        score.setTimeLeft(timeLeft);
        return score;
    }
}
