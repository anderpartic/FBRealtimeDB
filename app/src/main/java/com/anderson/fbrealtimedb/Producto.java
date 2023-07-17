package com.anderson.fbrealtimedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Producto extends AppCompatActivity {

    private TextInputEditText etCodigo, etNomProducto, etStock, etCosto, etVenta;
    private Button btnGuardar, btnActualizar, btnBuscar, btnEliminar, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productosRef = database.getReference("productos");

        etCodigo = findViewById(R.id.etCodigo);
        etNomProducto = findViewById(R.id.etNomProducto);
        etStock = findViewById(R.id.etStock);
        etCosto = findViewById(R.id.etCosto);
        etVenta = findViewById(R.id.etVenta);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);


        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Producto.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString();
                String nomProducto = etNomProducto.getText().toString();
                String stockStr = etStock.getText().toString();
                String costoStr = etCosto.getText().toString();
                String ventaStr = etVenta.getText().toString();

                // Verifica si algún campo está vacío
                if (codigo.isEmpty() || nomProducto.isEmpty() || stockStr.isEmpty() || costoStr.isEmpty() || ventaStr.isEmpty()) {
                    Toast.makeText(Producto.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int stock = Integer.parseInt(stockStr);
                double costo = Double.parseDouble(costoStr);
                double venta = Double.parseDouble(ventaStr);

                // Resto del código para guardar el producto en la base de datos...

                // Crea un HashMap para almacenar los datos del producto
                HashMap<String, Object> producto = new HashMap<>();
                producto.put("codigo", codigo);
                producto.put("nomProducto", nomProducto);
                producto.put("stock", stock);
                producto.put("costo", costo);
                producto.put("venta", venta);


                // Verifica si el código ya existe en la base de datos
                DatabaseReference codigoRef = productosRef.child(codigo);
                codigoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // El código ya existe, muestra un mensaje de error
                            Toast.makeText(Producto.this, "El código ya existe", Toast.LENGTH_SHORT).show();
                        } else {
                            // El código no existe, guarda el producto en la base de datos
                            codigoRef.setValue(producto);
                            Toast.makeText(Producto.this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();

                            // Limpia los campos de entrada después de guardar los datos
                            etCodigo.setText(null);
                            etNomProducto.setText(null);
                            etStock.setText(null);
                            etCosto.setText(null);
                            etVenta.setText(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Maneja el error en caso de que la lectura se cancele
                        Toast.makeText(Producto.this, "Error al verificar el código", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString();

                // Verifica si el campo de código está vacío
                if (codigo.isEmpty()) {
                    Toast.makeText(Producto.this, "Por favor, ingresa un código", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realiza la búsqueda del producto en la base de datos
                DatabaseReference codigoRef = productosRef.child(codigo);
                codigoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // El producto existe, obtén los datos y muestra la información
                            String nomProducto = dataSnapshot.child("nomProducto").getValue(String.class);
                            int stock = dataSnapshot.child("stock").getValue(Integer.class);
                            double costo = dataSnapshot.child("costo").getValue(Double.class);
                            double venta = dataSnapshot.child("venta").getValue(Double.class);

                            etNomProducto.setText(nomProducto);
                            etStock.setText(String.valueOf(stock));
                            etCosto.setText(String.valueOf(costo));
                            etVenta.setText(String.valueOf(venta));
                        } else {
                            // El producto no existe, muestra un mensaje de error
                            Toast.makeText(Producto.this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Maneja el error en caso de que la lectura se cancele
                        Toast.makeText(Producto.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString();
                String nomProducto = etNomProducto.getText().toString();
                String stockStr = etStock.getText().toString();
                String costoStr = etCosto.getText().toString();
                String ventaStr = etVenta.getText().toString();

                // Verifica si algún campo está vacío
                if (codigo.isEmpty() || nomProducto.isEmpty() || stockStr.isEmpty() || costoStr.isEmpty() || ventaStr.isEmpty()) {
                    Toast.makeText(Producto.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int stock = Integer.parseInt(stockStr);
                double costo = Double.parseDouble(costoStr);
                double venta = Double.parseDouble(ventaStr);

                // Crea un HashMap para almacenar los datos actualizados del producto
                HashMap<String, Object> producto = new HashMap<>();
                producto.put("codigo", codigo);
                producto.put("nomProducto", nomProducto);
                producto.put("stock", stock);
                producto.put("costo", costo);
                producto.put("venta", venta);

                // Actualiza el producto en la base de datos
                DatabaseReference codigoRef = productosRef.child(codigo);
                codigoRef.setValue(producto);

                Toast.makeText(Producto.this, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();

                // Limpia los campos de entrada después de actualizar los datos
                etCodigo.setText(null);
                etNomProducto.setText(null);
                etStock.setText(null);
                etCosto.setText(null);
                etVenta.setText(null);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString();

                // Verifica si el campo de código está vacío
                if (codigo.isEmpty()) {
                    Toast.makeText(Producto.this, "Por favor, ingresa un código", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Elimina el producto de la base de datos
                DatabaseReference codigoRef = productosRef.child(codigo);
                codigoRef.removeValue();

                Toast.makeText(Producto.this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();

                // Limpia los campos de entrada después de eliminar el producto
                etCodigo.setText(null);
                etNomProducto.setText(null);
                etStock.setText(null);
                etCosto.setText(null);
                etVenta.setText(null);
            }
        });

    }
}