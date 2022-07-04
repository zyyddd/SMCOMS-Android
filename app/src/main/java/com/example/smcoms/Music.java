package com.example.smcoms;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Music extends Service {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("action",-1);
        if (action == 0 ){
            mediaPlayer.start();
            Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
        }else if(action == 1){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        else {
            Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        Toast.makeText(this, "停止播放", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
