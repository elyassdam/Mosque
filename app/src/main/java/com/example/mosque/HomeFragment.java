package com.example.mosque;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private TextView textViewAboutUs;
    private Button btnAddContent;
    private HadithApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewAboutUs = view.findViewById(R.id.textViewAboutUs);
        btnAddContent = view.findViewById(R.id.btnAddContent);

        // Configuración de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thesunnah.com/v1/hadiths/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(HadithApiService.class);

        // Obtener y mostrar el Hadith del día
        getDailyHadith();

        // Manejar el botón para añadir contenido
        btnAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para abrir la actividad de añadir contenido
                Toast.makeText(getContext(), "Add Content button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getDailyHadith() {
        // Realizar la solicitud a la API
        apiService.getDailyHadith().enqueue(new Callback<HadithResponse>() {
            @Override
            public void onResponse(@NonNull Call<HadithResponse> call, @NonNull Response<HadithResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Hadith hadith = response.body().getData();
                    if (hadith != null) {
                        // Mostrar el Hadith del día
                        textViewAboutUs.setText(hadith.getBody());
                    } else {
                        textViewAboutUs.setText("No se encontró un Hadith para hoy");
                    }
                } else {
                    textViewAboutUs.setText("No se pudo obtener el Hadith del día");
                }
            }

            @Override
            public void onFailure(@NonNull Call<HadithResponse> call, @NonNull Throwable t) {
                textViewAboutUs.setText("Error: " + t.getMessage());
            }
        });
    }
}
