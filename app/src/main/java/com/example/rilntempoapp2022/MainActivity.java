package com.example.rilntempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    IEdfApi edfApi;

    // views
    private TextView redDaysTv;
    private TextView whiteDaysTv;
    private TextView blueDaysTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init views
        redDaysTv = findViewById(R.id.red_days_tv);
        whiteDaysTv = findViewById(R.id.white_days_tv);
        blueDaysTv = findViewById(R.id.blue_days_tv);

        // Init retrofit client
        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            // create EDF API call interface
            edfApi = retrofitClient.create(IEdfApi.class);
        }

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
}