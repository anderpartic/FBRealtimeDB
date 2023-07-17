package com.anderson.fbrealtimedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Persona extends AppCompatActivity {
    private TextInputEditText etEmail, etCedula, etNombre, etProvincia;

    private RadioButton rbHombre, rbMujer;

    private Spinner spPais;
    String[] Paises = {"Seleccione un país: ", "Ecuador", "Colombia", "Venezuela", "Chile", "Argentina"};
    private Button btnRegresar, btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);
        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etProvincia = findViewById(R.id.etProvincia);
        rbHombre = findViewById(R.id.rbHombre);
        rbMujer = findViewById(R.id.rbMujer);
        spPais = findViewById(R.id.spPais);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapter);
        etEmail = findViewById(R.id.etEmail);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });


        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Persona.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String mail = user.getEmail();
            etEmail.setText(mail);

        } else {
            getApplicationContext();
        }
    }

    private void actualizarDatos() {
        // Obtener los valores de los campos
        String cedula = etCedula.getText().toString();
        String nombre = etNombre.getText().toString();
        String provincia = etProvincia.getText().toString();
        boolean esHombre = rbHombre.isChecked();
        String pais = spPais.getSelectedItem().toString();

        // Obtener el ID del usuario actual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Crear una referencia al nodo del usuario en la base de datos
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Persona").child(userId);

        // Verificar si la cédula ya está registrada
        DatabaseReference cedulaRef = FirebaseDatabase.getInstance().getReference().child("Persona");
        cedulaRef.orderByChild("cedula").equalTo(cedula).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // La cédula ya está registrada
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String usuarioId = childSnapshot.getKey();
                        String email = childSnapshot.child("email").getValue(String.class);
                        if (!usuarioId.equals(userId) && email != null) {
                            // La cédula pertenece a otro usuario con un correo asociado
                            Toast.makeText(Persona.this, "La cédula ya está registrada para otro usuario", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                // La cédula no está asignada a otro usuario o pertenece al usuario actual, guardar los datos en la base de datos
                usuarioRef.child("cedula").setValue(cedula);
                usuarioRef.child("nombre").setValue(nombre);
                usuarioRef.child("provincia").setValue(provincia);
                usuarioRef.child("esHombre").setValue(esHombre);
                usuarioRef.child("pais").setValue(pais);

                Toast.makeText(Persona.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error al acceder a la base de datos
                Toast.makeText(Persona.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}