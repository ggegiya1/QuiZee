package com.app.game.quizee.backend;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.app.game.quizee.R;
/**
 *The class is greatly inspired of https://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
 * Allow us to manage the music in game
 */
public class MusicService extends Service  implements MediaPlayer.OnErrorListener {

    //Count to switch the music
    int compteur=1;
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;
    private boolean mustRestart;
    static MusicService musicServiceInstance;

    /**
     *Constructor of class MusicService
     */
    public MusicService() {
        musicServiceInstance = this;
        mustRestart = false;
    }

    /**
     *Allow to use the class without outside instanciation
     */
    public static synchronized MusicService getInstance() {
        if (musicServiceInstance == null){
            return new MusicService();
        }
        return musicServiceInstance;
    }

    /**
     *Allow interprocess communication within Android
     * Used to stop the music when the app is closed
     */
    public static class ServiceBinder extends Binder {
        public static MusicService getService() {
            if(musicServiceInstance == null) {
                return new MusicService();
            }
            return musicServiceInstance;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    /**
     *Start the music and get ready to catch errors
     */
    @Override
    public void onCreate() {
        super.onCreate();
        updateMusic();
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnErrorListener(new OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {
                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    public void start() {
        mPlayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean music = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("sound_music", false);
        if(music) {
            mPlayer.start();
        }
        return START_NOT_STICKY;
    }

    /**
     *Force the pause as long as the mediaplayer exists
     */
    public void inconditionalPauseMusic() {
        Log.i("Music", "music paused");
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                length = mPlayer.getCurrentPosition();
            }
        }
    }

    /**
     *TODO
     */
    public void pauseMusic() {
        if (!mustRestart) {
            inconditionalPauseMusic();
        }
        mustRestart = false;
    }

    public void resumeMusic(boolean mRestart) {
        boolean music = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("sound_music", false);
        if(music) {
            Log.i("Music", "music resumed");
            if (mPlayer == null) {
                updateMusic();
            } else if (!mPlayer.isPlaying()) {
                mPlayer.seekTo(length);
                mPlayer.start();
            }
        }
        mustRestart = mRestart;
    }

    public void stopMusic() {
        Log.i("Music", "music stopped");
        if(mPlayer.isPlaying()) {
            mPlayer.stop();
        }
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

    public void randomMusic(){
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



    @Override
    public void onTaskRemoved(Intent rootIntent){
        onDestroy();
        super.onTaskRemoved(rootIntent);
    }

    public void updateMusic(){
        this.mPlayer = new MediaPlayer();
        randomMusic();
        boolean music = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("sound_music", false);
        if(music) {
            this.mPlayer.start();
        }
        this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                updateMusic();
            }
        });
    }
}
