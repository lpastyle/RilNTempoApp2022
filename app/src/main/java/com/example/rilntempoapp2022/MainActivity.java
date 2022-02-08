package com.example.rilntempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    IEdfApi edfApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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