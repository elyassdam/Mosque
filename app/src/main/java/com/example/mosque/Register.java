package com.example.mosque;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextDni;
    private DatePickerDialog datePickerDialog;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button btnRegister;
    private Button btnBackLogin;

    private Button datePickerButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        datePickerButton = findViewById(R.id.datePickerButton);
        initDatePicker();
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        datePickerButton.setText(getTodaysDate());

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        editTextDni = findViewById(R.id.dni);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackLogin = findViewById(R.id.btnBack);
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                Toast.makeText(Register.this, "Cambiando a Inicio", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String dateOfBirth = datePickerButton.getText().toString();
                final String email = editTextEmail.getText().toString();
                final String phone = editTextPhone.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String confirmPassword = editTextConfirmPassword.getText().toString();
                final String dni = editTextDni.getText().toString();

                // Check if fields are empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                        TextUtils.isEmpty(dni)) {
                    Toast.makeText(Register.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is valid
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Register.this, "Introduce un email válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user in Firestore
                registerUser(name, dni, dateOfBirth, email, phone, password);
            }
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = makeDateString(dayOfMonth, month + 1, year);
                datePickerButton.setText(date);
            }
        };

        datePickerDialog = new DatePickerDialog(this, dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return "Ene"; // Default value if month is invalid
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }

    private void registerUser(final String name, final String dni, final String dateOfBirth, final String email,
                              final String phone, final String password) {
        // Check if the email is already registered in Firestore
        firestore.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    // The email is already registered, show error message
                    Toast.makeText(Register.this, "Email ya registrado", Toast.LENGTH_SHORT).show();
                } else {
                    // The email is not registered, create a new user
                    String userId = firestore.collection("users").document().getId(); // Generate a unique ID for the user
                    String hashedPassword = PasswordHasher.hashPassword(password);
                    User user = new User(userId, name, dni, email, phone, dateOfBirth, hashedPassword);

                    // Save the user to Firestore
                    firestore.collection("users").document(userId).set(user).addOnSuccessListener(aVoid -> {
                        Toast.makeText(Register.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Register.this, "Error registrando usuario", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                Toast.makeText(Register.this, "Error accediendo a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
