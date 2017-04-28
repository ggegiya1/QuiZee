package com.app.game.quizee.backend;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.View;

import com.app.game.quizee.BottomNavigation;
import com.app.game.quizee.R;

import java.util.Random;

/**
 * Created by Maude on 2017-04-28.
 */

public class PlayMusic {
    int compteur=1;
    MediaPlayer mymedia;
    Context bn;

    public PlayMusic(Application myapp, Context bn){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myapp);
        if(prefs.getBoolean("sound_music", false)) {
            this.bn= bn;
            updatemusic();
        }
    }

    public void randommusic(Context bn){
        switch (compteur) {
            case 1:
                this.mymedia = MediaPlayer.create(bn, R.raw.bg1);
                compteur+=1;
                break;
            case 2:
                this.mymedia = MediaPlayer.create(bn, R.raw.bg2);
                compteur+=1;
                break;
            case 3:
                this.mymedia = MediaPlayer.create(bn, R.raw.bg3);
                compteur+=1;
                break;
            default:
                this.mymedia = MediaPlayer.create(bn, R.raw.bg4);
                compteur =1;
                break;
        }
    }
    public void updatemusic(){
        /*if (this.mymedia != null) {
            if (this.mymedia.isPlaying()) {
                this.mymedia.stop();
                this.mymedia.reset();
            }
            this.mymedia.release();
        }*/
        this.mymedia = new MediaPlayer();
        randommusic(this.bn);
        this.mymedia.start();
        this.mymedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                updatemusic();
            }
        });
    }
}
