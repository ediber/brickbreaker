package com.example.brickbreaker04;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.slipknot); // Ensure you have a sample_music.mp3 file in res/raw
        mediaPlayer.setLooping(true); // Set looping
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Log.d(TAG, "MusicService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start playback
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d(TAG, "Music playback started");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop playback and release resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "Music playback stopped");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't provide binding, so return null
    }
}
