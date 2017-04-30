package com.app.game.quizee.backend;

/**
 * Created by trist on 2017-04-30.
 */

//La classe est grandement inspiré de https://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.app.game.quizee.R;

public class MusicService extends Service  implements MediaPlayer.OnErrorListener {

    int compteur=1;
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;
    static MusicService musicServiceInstance;

    public MusicService() {
    }

    public static synchronized MusicService getInstance() {
        if (musicServiceInstance == null){
            musicServiceInstance = new MusicService();
        }
        return musicServiceInstance;
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer.setOnErrorListener(this);

        mPlayer.setOnErrorListener(new OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (!mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public void randommusic(){
        switch (compteur) {
            case 1:
                this.mPlayer = MediaPlayer.create(this, R.raw.bg1);
                compteur++;
                break;
            case 2:
                this.mPlayer = MediaPlayer.create(this, R.raw.bg2);
                compteur++;
                break;
            case 3:
                this.mPlayer = MediaPlayer.create(this, R.raw.bg3);
                compteur++;
                break;
            default:
                this.mPlayer = MediaPlayer.create(this, R.raw.bg4);
                compteur=1;
                break;
        }
    }

    public void updatemusic(final Context c){
        /*if (this.mymedia != null) {
            if (this.mymedia.isPlaying()) {
                this.mymedia.stop();
                this.mymedia.reset();
            }
            this.mymedia.release();
        }*/
        this.mPlayer = new MediaPlayer();
        randommusic();
        this.mPlayer.start();
        this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                updatemusic(c);
            }
        });
    }
}
