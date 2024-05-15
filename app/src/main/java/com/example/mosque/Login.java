package com.example.mosque;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCorreo = findViewById(R.id.username);
        editTextContraseña = findViewById(R.id.password);

        Button btnRegistrarse = findViewById(R.id.btnRegistrar);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (esCorreoValido(correo) && esContraseñaValida(contraseña)) {
                    // Correo y contraseña válidos, continuar con el registro
                    Toast.makeText(Login.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar mensaje de error al usuario
                    Toast.makeText(Login.this, "Correo o contraseña inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean esCorreoValido(String correo) {
        // Verificar que contenga un '@'
        return correo.contains("@");
    }

    public boolean esContraseñaValida(String contraseña) {
        // Requerir al menos 6 caracteres
        if (contraseña.length() < 6) {
            return false;
        }

        // Requerir al menos un número y una letra
        boolean tieneNumero = false;
        boolean tieneLetra = false;
        for (char c : contraseña.toCharArray()) {
            if (Character.isDigit(c)) {
                tieneNumero = true;
            } else if (Character.isLetter(c)) {
                tieneLetra = true;
            }
            if (tieneNumero && tieneLetra) {
                return true;
            }
        }

        return false;
    }
}
