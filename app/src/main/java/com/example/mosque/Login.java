package com.example.mosque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;
    private CheckBox checkBoxRemember;
    private FirebaseFirestore firestore;
    private Spinner spinnerLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        editTextCorreo = findViewById(R.id.username);
        editTextContraseña = findViewById(R.id.password);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        Button btnIniciar = findViewById(R.id.btnInicio);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (correo.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    usuarioRegistrado(correo, contraseña);
                }
            }
        });

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Verificar si hay credenciales guardadas
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String correoGuardado = sharedPreferences.getString("correo", "");
        String contraseñaGuardada = sharedPreferences.getString("contraseña", "");
        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMeChecked", false);

        editTextCorreo.setText(correoGuardado);
        editTextContraseña.setText(contraseñaGuardada);
        checkBoxRemember.setChecked(rememberMeChecked);

        if (rememberMeChecked && !correoGuardado.isEmpty() && !contraseñaGuardada.isEmpty()) {
            // Intentar iniciar sesión automáticamente
            usuarioRegistrado(correoGuardado, contraseñaGuardada);
        }
    }

    private void usuarioRegistrado(String correo, String contraseña) {
        firestore.collection("users").whereEqualTo("email", correo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        User usuario = documentSnapshot.toObject(User.class);
                        if (usuario != null && PasswordHasher.verifyPassword(contraseña, usuario.getPassword())) {
                            // El usuario está registrado y la contraseña coincide
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            if (checkBoxRemember.isChecked()) {
                                // Guardar credenciales si se seleccionó "Remember Me"
                                SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("correo", correo);
                                editor.putString("contraseña", contraseña); // Consider not saving plain text password here
                                editor.putBoolean("rememberMeChecked", true);
                                editor.apply();
                            }
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // El usuario está registrado pero la contraseña no coincide
                    Toast.makeText(Login.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    // El usuario no está registrado
                    Toast.makeText(Login.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Manejar el error
                Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        // Reload current activity to apply the language change
        Intent refresh = new Intent(this, Login.class);
        startActivity(refresh);
        finish();
    }
}
