package com.example.mosque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCorreo = findViewById(R.id.username);
        editTextContraseña = findViewById(R.id.password);

        Button btnIniciar = findViewById(R.id.btnInicio);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (esCorreoValido(correo) && esContraseñaValida(contraseña)) {
                    usuarioRegistrado(correo, contraseña);
                } else {
                    Toast.makeText(Login.this, "Correo o contraseña inválidos", Toast.LENGTH_SHORT).show();
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

    private boolean esCorreoValido(String correo) {
        return correo.contains("@");
    }

    private boolean esContraseñaValida(String contraseña) {
        return contraseña.length() >= 6;
    }

    private void usuarioRegistrado(String correo, String contraseña) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("email").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User usuario = userSnapshot.getValue(User.class);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
                Toast.makeText(Login.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
