package com.example.mosque;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalatFragment extends Fragment {
    private TextView textViewFajr, textViewDhuhr, textViewAsr, textViewMaghrib, textViewIsha, textViewCity, textViewTimeRemaining;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salat, container, false);

        initializeTextViews(view);
        getSalatTimings("Collado Villalba", "Madrid", "Spain", 3);

        return view;
    }

    private void initializeTextViews(View view) {
        textViewFajr = view.findViewById(R.id.textViewFajr);
        textViewDhuhr = view.findViewById(R.id.textViewDhuhr);
        textViewAsr = view.findViewById(R.id.textViewAsr);
        textViewMaghrib = view.findViewById(R.id.textViewMaghrib);
        textViewIsha = view.findViewById(R.id.textViewIsha);
        textViewCity = view.findViewById(R.id.textViewCity);
        textViewTimeRemaining = view.findViewById(R.id.textViewTimeRemaining);
    }

    private void getSalatTimings(String pueblo, String city, String country, int method) {
        AlAdhanApiService apiService = RetrofitClient.getClient().create(AlAdhanApiService.class);
        Call<SalatResponse> call = apiService.getSalatTimingsByCity(pueblo, city, country, method);

        call.enqueue(new Callback<SalatResponse>() {
            @Override
            public void onResponse(Call<SalatResponse> call, Response<SalatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        updateTextViews(response.body().getData().getTimings());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SalatResponse> call, Throwable t) {
                textViewFajr.setText("Error: " + t.getMessage());
            }
        });
    }

    private void updateTextViews(SalatTimings timings) throws ParseException {
        textViewFajr.setText(timings.getFajr());
        textViewDhuhr.setText(timings.getDhuhr());
        textViewAsr.setText(timings.getAsr());
        textViewMaghrib.setText(timings.getMaghrib());
        textViewIsha.setText(timings.getIsha());

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date currentDate = new Date();
        String currentTimeStr = dateFormat.format(currentDate);

        Date currentTime = dateFormat.parse(currentTimeStr);

        String nextSalatTime = null;
        long timeDifferenceMillis = Long.MAX_VALUE;

        String[] salatTimes = {timings.getFajr(), timings.getDhuhr(), timings.getAsr(), timings.getMaghrib(), timings.getIsha()};
        String[] salatNames = {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"};

        for (int i = 0; i < salatTimes.length; i++) {
            Date salatTime = dateFormat.parse(salatTimes[i]);
            if (salatTime.after(currentTime) && (salatTime.getTime() - currentTime.getTime()) < timeDifferenceMillis) {
                nextSalatTime = salatNames[i];
                timeDifferenceMillis = salatTime.getTime() - currentTime.getTime();
            }
        }

        if (nextSalatTime != null) {
            long totalTimeMillis = timeDifferenceMillis;
            String finalNextSalatTime = nextSalatTime;
            countDownTimer = new CountDownTimer(totalTimeMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000 % 60;
                    long minutes = millisUntilFinished / (1000 * 60) % 60;
                    long hours = millisUntilFinished / (1000 * 60 * 60) % 24;
                    String timeRemaining = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    textViewTimeRemaining.setText("Tiempo restante para " + finalNextSalatTime + ": " + timeRemaining);
                }

                @Override
                public void onFinish() {
                    textViewTimeRemaining.setText("Tiempo restante para " + finalNextSalatTime + ": 00:00:00");
                }
            }.start();
        } else {
            textViewTimeRemaining.setText("No hay una próxima salat");
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonOpenQiblaCompass = view.findViewById(R.id.buttonOpenQiblaCompass);
        buttonOpenQiblaCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QiblaCompassActivity.class);
                startActivity(intent);
            }
        });
    }
}
