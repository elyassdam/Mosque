package com.example.mosque;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArabicClassesFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView textViewUserEmail;
    private EditText editTextStudentName, editTextStudentSurname, editTextStudentDNI, editTextStudentBirthdate, editTextStudentGender;
    private EditText editTextParent1DNI, editTextParent1Name, editTextParent1Surname, editTextParent2DNI, editTextParent2Name, editTextParent2Surname, editTextAddress, editTextBankAccount;
    private Button buttonSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        editTextStudentName = view.findViewById(R.id.editTextStudentName);
        editTextStudentSurname = view.findViewById(R.id.editTextStudentSurname);
        editTextStudentDNI = view.findViewById(R.id.editTextStudentDNI);
        editTextStudentBirthdate = view.findViewById(R.id.editTextStudentBirthdate);
        editTextStudentGender = view.findViewById(R.id.editTextStudentGender);
        editTextParent1DNI = view.findViewById(R.id.editTextParent1DNI);
        editTextParent1Name = view.findViewById(R.id.editTextParent1Name);
        editTextParent1Surname = view.findViewById(R.id.editTextParent1Surname);
        editTextParent2DNI = view.findViewById(R.id.editTextParent2DNI);
        editTextParent2Name = view.findViewById(R.id.editTextParent2Name);
        editTextParent2Surname = view.findViewById(R.id.editTextParent2Surname);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextBankAccount = view.findViewById(R.id.editTextBankAccount);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);

        // Set the current user's email
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            textViewUserEmail.setText("Usuario: " + currentUser.getEmail());
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    submitStudentData();
                }
            }
        });

        return view;
    }

    private boolean validateInputs() {
        String studentDNI = editTextStudentDNI.getText().toString().trim();
        String studentBirthdate = editTextStudentBirthdate.getText().toString().trim();
        String parent1DNI = editTextParent1DNI.getText().toString().trim();
        String parent2DNI = editTextParent2DNI.getText().toString().trim();

        // Validación de DNI
        String dniPattern = "\\d{8}[A-Za-z]";
        if (!Pattern.matches(dniPattern, studentDNI)) {
            editTextStudentDNI.setError("DNI inválido. Debe tener 8 números y una letra.");
            return false;
        }
        if (!Pattern.matches(dniPattern, parent1DNI)) {
            editTextParent1DNI.setError("DNI inválido. Debe tener 8 números y una letra.");
            return false;
        }
        if (!Pattern.matches(dniPattern, parent2DNI)) {
            editTextParent2DNI.setError("DNI inválido. Debe tener 8 números y una letra.");
            return false;
        }

        // Validación de fecha de nacimiento
        if (!isValidDate(studentBirthdate)) {
            editTextStudentBirthdate.setError("Fecha de nacimiento inválida. Use el formato dd/MM/yyyy.");
            return false;
        }

        return true;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

    private void submitStudentData() {
        String studentName = editTextStudentName.getText().toString().trim();
        String studentSurname = editTextStudentSurname.getText().toString().trim();
        String studentDNI = editTextStudentDNI.getText().toString().trim();
        String studentBirthdate = editTextStudentBirthdate.getText().toString().trim();
        String studentGender = editTextStudentGender.getText().toString().trim();
        String parent1DNI = editTextParent1DNI.getText().toString().trim();
        String parent1Name = editTextParent1Name.getText().toString().trim();
        String parent1Surname = editTextParent1Surname.getText().toString().trim();
        String parent2DNI = editTextParent2DNI.getText().toString().trim();
        String parent2Name = editTextParent2Name.getText().toString().trim();
        String parent2Surname = editTextParent2Surname.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String bankAccount = editTextBankAccount.getText().toString().trim();
        String userEmail = textViewUserEmail.getText().toString().trim();

        Map<String, Object> studentData = new HashMap<>();
        studentData.put("studentName", studentName);
        studentData.put("studentSurname", studentSurname);
        studentData.put("studentDNI", studentDNI);
        studentData.put("studentBirthdate", studentBirthdate);
        studentData.put("studentGender", studentGender);
        studentData.put("parent1DNI", parent1DNI);
        studentData.put("parent1Name", parent1Name);
        studentData.put("parent1Surname", parent1Surname);
        studentData.put("parent2DNI", parent2DNI);
        studentData.put("parent2Name", parent2Name);
        studentData.put("parent2Surname", parent2Surname);
        studentData.put("address", address);
        studentData.put("bankAccount", bankAccount);
        studentData.put("userEmail", userEmail);

        // Añadir los datos del estudiante al Firestore
        db.collection("students")
                .add(studentData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Los datos del estudiante se añadieron exitosamente
                        Toast.makeText(getActivity(), "Datos del estudiante guardados correctamente.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al añadir los datos del estudiante
                        Toast.makeText(getActivity(), "Error al guardar los datos del estudiante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
