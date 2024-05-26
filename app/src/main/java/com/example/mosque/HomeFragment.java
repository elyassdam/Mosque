package com.example.mosque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewProjects;
    private FloatingActionButton fab;

        private String selectedAppointmentType = "";

        public HomeFragment() {
            // Constructor público requerido
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflar el diseño del fragmento
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            // Configurar el OnClickListener para el botón de selección de tipo de cita
            Button selectAppointmentTypeButton = rootView.findViewById(R.id.buttonSelectAppointmentType);
            selectAppointmentTypeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mostrar el diálogo de selección de tipo de cita
                    showAppointmentTypeDialog();
                }
            });

            return rootView;
        }

        private void showAppointmentTypeDialog() {
            // Crear un arreglo de opciones para el diálogo
            final String[] appointmentTypes = {"Imam", "Administrador","Mezquita"};

            // Crear un diálogo de selección
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Seleccione el tipo de cita");
            builder.setItems(appointmentTypes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    // Obtener el tipo de cita seleccionado por el usuario
                    selectedAppointmentType = appointmentTypes[position];
                    Toast.makeText(requireContext(), "Tipo de cita seleccionado: " + selectedAppointmentType, Toast.LENGTH_SHORT).show();

                    // Después de seleccionar el tipo de cita, se puede proceder a seleccionar la fecha aquí si se desea
                    // showDatePickerDialog();
                }
            });

            // Mostrar el diálogo
            builder.show();
        }
    }