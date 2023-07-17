package com.anderson.fbrealtimedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Crear extends AppCompatActivity {

    TextView btnVolver;
    TextInputEditText etEmail, etPass, etConPass;
    Button btnGuardar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guardar();
            }
        });

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Crear.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void Guardar() {
        String email, pass, conPass;
        email = String.valueOf(etEmail.getText());
        pass = String.valueOf(etPass.getText());
        conPass = String.valueOf(etConPass.getText());
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Crear.this, "Ingresar Mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Crear.this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(Crear.this, "Ingresar Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(Crear.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(conPass)) {
            Toast.makeText(Crear.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo electrónico ya está registrado
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                // El correo electrónico ya está registrado
                                Toast.makeText(Crear.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                            } else {
                                // El correo electrónico no está registrado, crear el usuario en Firebase Authentication
                                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Registro exitoso en Firebase Authentication
                                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                                    DatabaseReference personaRef = FirebaseDatabase.getInstance().getReference().child("Persona").child(userId);
                                                    personaRef.child("email").setValue(email)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(Crear.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                                                        Intent i = new Intent(Crear.this, MainActivity.class);
                                                                        startActivity(i);
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(Crear.this, "Fallo al guardar la información en la base de datos", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(Crear.this, "Fallo en Autenticación", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error al verificar el correo electrónico
                            Toast.makeText(Crear.this, "Error al verificar el correo electrónico", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}