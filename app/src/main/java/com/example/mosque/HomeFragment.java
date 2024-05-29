package com.example.mosque;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private Button btnSelectDate;
    private Spinner spinnerAppointmentType;
    private String selectedAppointmentType = "Imam";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnSelectDate = rootView.findViewById(R.id.btnSelectDate);
        spinnerAppointmentType = rootView.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.dateTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAppointmentType.setAdapter(adapter);

        spinnerAppointmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAppointmentType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAppointmentType = "Imam"; // Valor por defecto
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        return rootView;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                if (isDateAvailable(selectedDate)) {
                    Toast.makeText(requireContext(), "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                    // Aquí puedes manejar la fecha seleccionada
                } else {
                    Toast.makeText(requireContext(), "Fecha no disponible, por favor seleccione otra fecha.", Toast.LENGTH_SHORT).show();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // No permitir seleccionar fechas pasadas
        datePickerDialog.show();
    }

    private boolean isDateAvailable(Calendar date) {
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        switch (selectedAppointmentType) {
            case "Imam":
                return dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY;
            case "Administrador":
                return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
            case "Mezquita":
                return isFridayAvailable(date);
            default:
                return false;
        }
    }

    private boolean isFridayAvailable(Calendar date) {
        if (date.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            return false;
        }

        Calendar firstFriday = getFirstFridayOfMonth(date);
        Calendar secondFriday = (Calendar) firstFriday.clone();
        secondFriday.add(Calendar.WEEK_OF_YEAR, 1);

        return date.equals(firstFriday) || date.equals(secondFriday);
    }

    private Calendar getFirstFridayOfMonth(Calendar date) {
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);

        int dayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
        int offset = (Calendar.FRIDAY - dayOfWeek + 7) % 7;
        firstDayOfMonth.add(Calendar.DAY_OF_MONTH, offset);

        return firstDayOfMonth;
    }
}
