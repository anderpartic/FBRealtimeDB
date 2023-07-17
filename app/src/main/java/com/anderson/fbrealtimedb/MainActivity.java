package com.anderson.fbrealtimedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextInputEditText etEmail, etPass;
    Button btnIniciar;

    private Spinner spTipo;
    String[] Tipo = {"Tipo de usuario: ", "Persona", "Producto", "Inventario"};
    TextView btnRegistro;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        spTipo = findViewById(R.id.spTipo);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Tipo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IniciarS();
                if (validarSeleccion()) {
                    IniciarS();
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione un tipo de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Crear.class);
                startActivity(i);
            }
        });

    }
    private boolean validarSeleccion() {
        int selectedPosition = spTipo.getSelectedItemPosition();
        return selectedPosition != 0;
    }

    public void IniciarS() {
        String mail, pass;
        mail = String.valueOf(etEmail.getText());
        pass = String.valueOf(etPass.getText());

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(MainActivity.this, "Ingresar Mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(MainActivity.this, "Ingresar Password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Inicio exitoso", Toast.LENGTH_SHORT).show();
                            int selectedPosition = spTipo.getSelectedItemPosition();
                            switch (selectedPosition) {
                                case 1: // Persona
                                    Intent personaIntent = new Intent(MainActivity.this, Persona.class);
                                    startActivity(personaIntent);
                                    break;
                                case 2: // Producto
                                    Intent productoIntent = new Intent(MainActivity.this, Producto.class);
                                    startActivity(productoIntent);
                                    break;
                                case 3: // Inventario
                                    Intent inventarioIntent = new Intent(MainActivity.this, Inventario.class);
                                    startActivity(inventarioIntent);
                                    break;
                            }
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Error de autentificación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}