package com.example.mosque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        editTextCorreo = findViewById(R.id.username);
        editTextContraseña = findViewById(R.id.password);

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
    }

    private void usuarioRegistrado(String correo, String contraseña) {
        firestore.collection("users").whereEqualTo("email", correo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        User usuario = documentSnapshot.toObject(User.class);
                        if (usuario != null && usuario.getPassword().equals(contraseña)) {
                            // El usuario está registrado y la contraseña coincide
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
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
}
