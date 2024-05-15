package com.example.mosque;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextdni;

    private DatePickerDialog datePickerDialog;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private Button btnRegister;
    private Button datePickerButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

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
        editTextdni=findViewById(R.id.confirmPassword);
        editTextdni=findViewById(R.id.dni);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String dateOfBirth = datePickerButton.getText().toString();
                final String email = editTextEmail.getText().toString();
                final String phone = editTextPhone.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String confirmPassword = editTextConfirmPassword.getText().toString();
                final String dni = editTextdni.getText().toString();
                // Check if fields are empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user in Firebase database
                registerUser(name,dni,email, dateOfBirth, phone, password,confirmPassword);
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
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return "Jan"; // Default value if month is invalid
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }

    private void registerUser(final String name,final String dni, final String dateOfBirth, final String email,
                              final String phone, final String password,final String confirmPassword) {
        // Check if the email is already registered in the Firebase database
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The email is already registered, show error message
                    Toast.makeText(Register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // The email is not registered, create a new user
                    String userId = databaseReference.push().getKey(); // Generate a unique ID for the user
                    User user = new User(userId,name,dni,email,phone, dateOfBirth, password,confirmPassword);

                    // Save the user to the Firebase database
                    databaseReference.child(userId).setValue(user);

                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    // Here you can add logic to automatically log in after registration
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors
                Toast.makeText(Register.this, "Error accessing database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
