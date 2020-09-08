package com.csc4320.activityTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class TimerActivity extends AppCompatActivity {

    private TextToSpeech tts;
    ArrayList<ActivityWorkout> activityRecords;
    Consumer<Void> finishCallback, cancelCallback;
    ProgressBar progressBar;
    TextView timing, activityName;
    String TAG = "TimerActivty";

    private void setProgress(int totalSecs, int secsRemaining, String activity) {
        progressBar.setProgress((int) (100 - secsRemaining * 100.0 / totalSecs));
        timing.setText(secsRemaining + " Secs");
        activityName.setText(activity);
    }

    // Work on this only
    private void showTimer(ActivityWorkout activity) {

        CountDownTimer timer = new CountDownTimer(activity.getSeconds() * 1000  + 1000, 100) {

            @Override
            public void onTick(long l) {
                l = l / 1000; // get seconds.
                setProgress(activity.getSeconds(), (int) l, activity.getWorkOutName());
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {
                setProgress(activity.getSeconds(), 0, "Completed");
            }
        };

        setProgress(activity.getSeconds(), activity.getSeconds(), activity.getWorkOutName());
        speak(activity.getWorkOutName());
        timer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              setContentView(R.layout.timer_activity);
        progressBar = findViewById(R.id.progressBar);
        timing = findViewById(R.id.timing);
        activityName = findViewById(R.id.activityName);

        activityRecords = (ArrayList<ActivityWorkout>) getIntent().getSerializableExtra("data");

        tts = new TextToSpeech(this, status -> {
            Log.i(TAG, "TTS " + status);
            if(status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        // Use MessageQueue to post messages with specific delay
        Looper looper = this.getMainLooper();

        Handler handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "Handling messageId: " + msg.what);
                showTimer(activityRecords.get(msg.what));
            }
        };

        long delay = 1000;  // ms
        for(int i=0; i<activityRecords.size(); i++) {
            int activityIndex = i;
            handler.postDelayed(() -> {
                handler.sendEmptyMessage(activityIndex);
            }, delay);
            delay += activityRecords.get(i).getSeconds() * 1000 + 1000;
        }

    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        if(tts != null){
            tts.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(tts != null){
            tts.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }
}