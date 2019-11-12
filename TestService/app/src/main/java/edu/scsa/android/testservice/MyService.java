package edu.scsa.android.testservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    MediaPlayer player;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "서비스 생성...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 이미 생성되었다면
        Toast.makeText(this, "서비스 제공...", Toast.LENGTH_SHORT).show();
//        if (intent != null) {
//            String str = intent.getStringExtra("url");
//            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//            Log.i("INFO", "나의 정체는  : " + Thread.currentThread().getName());


            if (player != null) {
                player.stop();
                player.release();
            }
            player = MediaPlayer.create(this, R.raw.aa);
            player.setLooping(false);
            player.start();

//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // context. Service가 있고, 그 위에 Context가 있음.
        Toast.makeText(this, "서비스 종료...", Toast.LENGTH_SHORT).show();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
