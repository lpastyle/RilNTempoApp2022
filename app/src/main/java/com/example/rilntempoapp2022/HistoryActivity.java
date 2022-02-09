package com.example.rilntempoapp2022;

import static com.example.rilntempoapp2022.Tools.getNowDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();

    private RecyclerView tempoDateRv;
    private TempoDateAdapter tempoDateAdapter;

    // Data model
    List<TempoDate> tempoDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // find view
        tempoDateRv = findViewById(R.id.tempo_history_rv);

        // Init recycler view
        tempoDateRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tempoDateRv.setLayoutManager(layoutManager);

        tempoDateAdapter = new TempoDateAdapter(tempoDates);
        tempoDateRv.setAdapter(tempoDateAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTempoDates();
    }

    private void refreshTempoDates() {
        if (MainActivity.edfApi != null) {

            // get date range
            String yearNow = getNowDate("yyyy");
            String yearBefore ="";
            try {
                yearBefore = String.valueOf(Integer.parseInt(yearNow) - 1);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG,"unable to parse date "+ yearNow);
            }

            // Create API call
            Call<TempoHistory> call = MainActivity.edfApi.getTempoHistory(yearBefore, yearNow);

            // Launch async call
            call.enqueue(new Callback<TempoHistory>() {
                @Override
                public void onResponse(Call<TempoHistory> call, Response<TempoHistory> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        TempoHistory tempoHistory = response.body();
                        Log.d(LOG_TAG, "nb elements = "+ tempoHistory.getTempoDates().size());
                        //showTempoHistoryDates(tempoHistory.getTempoDates());
                    }
                }

                @Override
                public void onFailure(Call<TempoHistory> call, Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoHistory' failed");
                }
            });

        }
    }


    // -------------- HELPER FUNCTIONS ---------------

    private void showTempoHistoryDates(List<TempoDate> tempoDates) {
        for(TempoDate tempoDate : tempoDates) {
            Log.d(LOG_TAG,"Date="+tempoDate.getDate()+" Color="+tempoDate.getCouleur());
        }
    }

}