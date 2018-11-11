package com.company.schedule.ui.updateNote;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.R;

public class TimerFragment extends Fragment {
    Button b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentTimer = inflater.inflate(R.layout.fragment_timer, container, false);

        timerTextView = (TextView) fragmentTimer.findViewById(R.id.tvTimer);

        b = (Button) fragmentTimer.findViewById(R.id.btnTimer);
        return fragmentTimer;
    }

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = startTime - System.currentTimeMillis();
            if (millis<0)
            {
                millis = 0;
                stopTimer();
                Toast.makeText(getContext(), "You finish task", Toast.LENGTH_SHORT).show();
            }
            int seconds = (int) (millis / 1000);
            millis %= 1000;
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d.%04d", minutes, seconds, millis));

            timerHandler.postDelayed(this, 10);  // delay for update timer
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        b.setText("start");
        b.setOnClickListener(v -> {  // to stop the timer
            Button b = (Button) v;
            switch (b.getText().toString()) {
                case "start":
                    startTimer();
                    break;
   /*             case "pause":
                    pauseTimer();
                    break;
                case "resume":
                    resumeTimer();
                    break;
     */           case "stop":
                    stopTimer();
                    break;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //timerHandler.removeCallbacks(timerRunnable);
        //b.setText("start");
    }

    private void startTimer()
    {
        startTime = System.currentTimeMillis()+10*1000;
        timerHandler.postDelayed(timerRunnable, 0);
        b.setText("stop");
        //b.setText("pause");
    }
/*
    private void pauseTimer()
    {
        timerHandler.removeCallbacks(timerRunnable);
        b.setText("resume");
    }

    private void resumeTimer()
    {
        //startTime = System.currentTimeMillis()+5*1000;
        timerHandler.postDelayed(timerRunnable, 0);
        b.setText("pause");
    }
*/

    private void stopTimer()
    {
        timerHandler.removeCallbacks(timerRunnable);
        b.setText("start");
    }
}
