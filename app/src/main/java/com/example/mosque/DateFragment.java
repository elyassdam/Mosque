package com.example.mosque;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class DateFragment extends Fragment {

    private DatePicker datePicker;
    private Spinner spinnerCita;
    private Button btnConfirmarCita;
    private TextView tvSelectedDate;
    private FirebaseFirestore firestore;
    private String selectedCitaType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);

        firestore = FirebaseFirestore.getInstance();
        datePicker = view.findViewById(R.id.datePicker);
        spinnerCita = view.findViewById(R.id.spinnerCita);
        btnConfirmarCita = view.findViewById(R.id.btnConfirmarCita);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);

        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

        spinnerCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCitaType = parent.getItemAtPosition(position).toString();
                updateDatePicker();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConfirmarCita.setOnClickListener(v -> confirmarCita());

        return view;
    }

    private void updateDatePicker() {
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Example logic: this part should be modified to highlight the valid days for selection
        for (int i = 0; i < datePicker.getChildCount(); i++) {
            View child = datePicker.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextColor(isValidDate(dayOfWeek) ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }

    private void confirmarCita() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        if (isValidDate(selectedDate.get(Calendar.DAY_OF_WEEK))) {
            String date = day + "/" + (month + 1) + "/" + year;
            tvSelectedDate.setText("Cita seleccionada: " + date);

            // Guarda la cita en Firestore
            String userName = User.class.getName().toString(); // Aquí debes obtener el nombre de usuario actual
            Cita cita = new Cita(userName, date, selectedCitaType);
            firestore.collection("citas").add(cita)
                    .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Cita confirmada", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al confirmar cita", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Fecha no válida para " + selectedCitaType, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(int dayOfWeek) {
        switch (selectedCitaType) {
            case "Imam":
                return dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY;
            case "Administrador":
                return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
            case "Mezquita":
                return dayOfWeek == Calendar.FRIDAY;
            default:
                return false;
        }
    }

    static class Cita {
        private String userName;
        private String date;
        private String type;

        public Cita() {
        }

        public Cita(String userName, String date, String type) {
            this.userName = userName;
            this.date = date;
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public String getDate() {
            return date;
        }

        public String getType() {
            return type;
        }
    }
}
