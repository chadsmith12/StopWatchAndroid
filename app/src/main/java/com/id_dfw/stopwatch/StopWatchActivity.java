package com.id_dfw.stopwatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

import java.util.Random;


public class StopWatchActivity extends Activity {

    private int seconds = 0;
    // is the stop watch running
    private boolean running;
    private boolean wasRunning;
    // used for when we are changing colors
    private int colorTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        // we saved some values when the app was destroyed
        if(savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            colorTime = savedInstanceState.getInt("colorTime");
        }
        runTimer();
    }

    // if the activity is paused, stop the stop watch
    @Override
    protected void onPause() {
        super.onPause();
        // store if we need to continue running the stop watch or not when we come back into foreground
        wasRunning = running;
        // stop it from running in the background
        //running = false;
    }

    //activity is resumed
    @Override
    protected void onResume() {
        super.onResume();

        // only continue running the time if the we are supposed too.
        if(wasRunning) {
            running = true;
        }
    }

    // called when their is a configuration change, or anytime onDestroy is called
    // save how many seconds have passed by and if the app was running or not
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        savedInstanceState.putInt("colorTime", colorTime);
    }

    // user is starting the timer
    public void onClickStart(View view) {
        running = true;
    }

    // user is stopping/pausing the timer
    public void onClickStop(View view) {
        running = false;
    }

    // user is resetting the timer
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    // runs the actual timer and updates the text view
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.time_view);
        // create a new handler to handle the code we need to run
        final Handler handler = new Handler();

        handler.post(new Runnable() {
           @Override
           public void run() {
               // calculate the amount of hours
               int hours = seconds / 3600;
               int minutes = (seconds % 3600) / 60;
               int secs = seconds % 60;

               // format the timer
               String time = String.format("%d:%02d:%02d", hours, minutes, secs);
               timeView.setText(time);

               // if the timer is running go ahead and increatment the number of seconds it's been running
               if(running){
                   seconds++;
                   colorTime++;

                   // reset color time to zero and change the color when it's been a minute
                   if(colorTime >= 60) {
                       Random r = new Random();
                       int newColor = r.nextInt(10 - 0 + 1) + 1;
                       timeView.setTextColor(getColor(newColor));
                       colorTime = 0;
                   }
               }

               handler.postDelayed(this, 1000);
           }
        });
    }

    // returns a color based on a number
    private int getColor(int number) {
        switch(number) {
            case 0: return Color.CYAN;

            case 1: return Color.BLUE;

            case 2: return Color.GREEN;

            case 3: return Color.RED;

            default: return Color.BLACK;
        }
    }
}
