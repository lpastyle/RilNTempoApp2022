package com.example.rilntempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static IEdfApi edfApi;

    // views
    private TextView redDaysTv;
    private TextView whiteDaysTv;
    private TextView blueDaysTv;
    private DayColorView todayDcv;
    private DayColorView tomorrowDcv;
    private Button historyBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init views
        redDaysTv = findViewById(R.id.red_days_tv);
        whiteDaysTv = findViewById(R.id.white_days_tv);
        blueDaysTv = findViewById(R.id.blue_days_tv);
        todayDcv = findViewById(R.id.today_dcv);
        tomorrowDcv = findViewById(R.id.tomorrow_dcv);
        historyBt = findViewById(R.id.history_bt);
        historyBt.setOnClickListener(this);

        // Init retrofit client
        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            // create EDF API call interface
            edfApi = retrofitClient.create(IEdfApi.class);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNbTempoDaysLeft();
        updateNbTempoDaysColors();
    }

    private void updateNbTempoDaysColors() {
        if (edfApi != null) {

            // create call
            Call<TempoDaysColor> call = edfApi.getTempoDaysColor(Tools.getNowDate("yyyy-MM-dd"), IEdfApi.EDF_TEMPO_ALERT_TYPE);

            // launch call
            call.enqueue(new Callback<TempoDaysColor>() {
                @Override
                public void onResponse(@NonNull Call<TempoDaysColor> call, @NonNull Response<TempoDaysColor> response) {
                    TempoDaysColor tempoDaysColor = response.body();
                    if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysColor != null) {
                        Log.d(LOG_TAG, "J day color = " + tempoDaysColor.getJourJ().getTempo().toString());
                        Log.d(LOG_TAG, "J1 day color = " + tempoDaysColor.getJourJ1().getTempo().toString());
                        todayDcv.setDayColor(tempoDaysColor.getJourJ().getTempo());
                        tomorrowDcv.setDayColor(tempoDaysColor.getJourJ1().getTempo());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempoDaysColor> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysColor' request failed");
                }
            });
        }
    }

    private void updateNbTempoDaysLeft() {
        if (edfApi != null) {
            // Create call
            Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(IEdfApi.EDF_TEMPO_ALERT_TYPE);

            // Launch asynchronous call
            call.enqueue(new Callback<TempoDaysLeft>() {
                @Override
                public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                    TempoDaysLeft tempoDaysLeft = response.body();
                    if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysLeft != null) {
                        Log.d(LOG_TAG,"nb red days = " + tempoDaysLeft.getParamNbJRouge());
                        Log.d(LOG_TAG,"nb white days = " + tempoDaysLeft.getParamNbJBlanc());
                        Log.d(LOG_TAG,"nb blue days = " + tempoDaysLeft.getParamNbJBleu());
                        redDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJRouge()));
                        whiteDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJBlanc()));
                        blueDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJBleu()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysLeft' request failed");
                }
            });

        }
    }

  /* obsolete way of proceeding with XML attribute 'onclick'
   public void showHistory(View view) {
        Log.d(LOG_TAG,"showHistory()");
        Intent intent = new Intent();
        intent.setClass(this, HistoryActivity.class);
        startActivity(intent);
    } */

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "onClick()");
        if (view.getId() == R.id.history_bt) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivity.class);
            startActivity(intent);
        }
    }
}