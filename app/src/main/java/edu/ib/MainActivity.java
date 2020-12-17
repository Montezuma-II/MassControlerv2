package edu.ib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalTime;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds;
    private boolean running;
    private boolean wasRunning;
    private double mass;
    private int period;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            mass = savedInstanceState.getDouble("mass");
            period = savedInstanceState.getInt("period");
        }
        runTimer();
    }

    private void runTimer() {

        final TextView timeView = (TextView) findViewById(R.id.tvTime);
        final TextView mView = (TextView) findViewById(R.id.massLabel);
        final TextView pView = (TextView) findViewById(R.id.periodLabel);


        final EditText mIn = (EditText) findViewById(R.id.massInput);
        final EditText pIn = (EditText) findViewById(R.id.periodInput);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             int hours = seconds / 3600;
                             int minutes = (seconds % 3600) / 60;
                             int secs = seconds % 60;

                             String time = String.format(Locale.getDefault(),
                                     "%d:%02d:%02d", hours, minutes, secs);
                             timeView.setText(time);

                             Log.d("licznik", String.valueOf(seconds));
                             try {
                                 mass = Double.valueOf(mIn.getText().toString());
                                 period = Integer.valueOf(pIn.getText().toString());
                             }catch (IllegalArgumentException exception){
                                 if (mIn.getText().toString().equals("")) {
                                     mIn.setHint("Pleas input your mass!");
                                 }
                                 if (pIn.getText().toString().equals(null)|| period<=0) {
                                     pIn.setHint("You must input your time period!");
                                 }
                             }
                             String pStr = "";
                             int periodOut = 0;
                             if (running) {
                                 seconds++;
                                 double p = Double.valueOf(period);
                                 double massI = 0;

                                 String sign = pIn.getText().toString();

                                 //Moduł obliczający mase:

                                 periodOut = seconds / period;
                                 pView.setText("Period: " + periodOut);

                                 switch (sign) {
                                     case "10": {
                                         massI = (1 / p) * seconds * 51;
                                     }
                                     case "3": {
                                         massI = ((1 / p) * seconds * 22);
                                     }
                                     case "5": {
                                         massI = ((1 / p) * seconds * 17);
                                     }
                                     case "14": {
                                         massI = ((1 / p) * seconds * 11);
                                     }
                                     String format = String.format("%.3f", (mass - massI));
                                     mView.setText("Mass: " + format);
                                 }
                                 if ((mass - massI) <= 0) {
                                     running = false;
                                     mView.setText("Mass: 0");// zatrzymuje obliczanie rozkładu
                                 }
                             }
                             handler.postDelayed(this, 1000);
                         }

                     }
        );
    }

    public void onClickStart(View view) {

        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (wasRunning)
            running = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstancetState) {
        super.onSaveInstanceState(savedInstancetState);
        savedInstancetState.putInt("seconds", seconds);
        savedInstancetState.putInt("period", period);
        savedInstancetState.putDouble("mass", mass);
        savedInstancetState.putBoolean("running", running);
        savedInstancetState.putBoolean("wasRunning", wasRunning);


    }
}