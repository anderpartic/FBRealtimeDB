package com.anderson.fbrealtimedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Compras extends AppCompatActivity {

    private TextInputEditText etCodigo, tvNombre, tvStock, tvPrecioVenta, etCantidad, tvTotal;

    private Button btnBuscar, btnTotal, btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productosRef = database.getReference("productos");

        etCodigo = findViewById(R.id.etCodigo);
        tvNombre = findViewById(R.id.tvNombre);
        tvStock = findViewById(R.id.tvStock);
        tvPrecioVenta = findViewById(R.id.tvPrecioVenta);
        etCantidad = findViewById(R.id.etCantidad);
        tvTotal = findViewById(R.id.tvTotal);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString();

                // Verifica si el campo de código está vacío
                if (codigo.isEmpty()) {
                    Toast.makeText(Compras.this, "Por favor, ingresa un código", Toast.LENGTH_SHORT).show();
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
                            double venta = dataSnapshot.child("venta").getValue(Double.class);

                            tvNombre.setText(nomProducto);
                            tvStock.setText(String.valueOf(stock));
                            tvPrecioVenta.setText(String.valueOf(venta));

                        } else {
                            // El producto no existe, muestra un mensaje de error
                            Toast.makeText(Compras.this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
                            limpiar();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Maneja el error en caso de que la lectura se cancele
                        Toast.makeText(Compras.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnTotal = findViewById(R.id.btnTotal);
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidadStr = etCantidad.getText().toString();

                if (cantidadStr.isEmpty()) {
                    Toast.makeText(Compras.this, "Por favor, ingresa una cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                int cantidad = Integer.parseInt(cantidadStr);
                double precioVenta = Double.parseDouble(tvPrecioVenta.getText().toString());
                double total = cantidad * precioVenta;
                tvTotal.setText(String.valueOf(total));
            }
        });

        btnComprar = findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidadStr = etCantidad.getText().toString();

                if (cantidadStr.isEmpty()) {
                    Toast.makeText(Compras.this, "Por favor, ingresa una cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int cantidad = Integer.parseInt(cantidadStr);

                // Obtener el stock actual del producto
                DatabaseReference productoRef = productosRef.child(etCodigo.getText().toString()).child("stock");
                productoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer stockActual = dataSnapshot.getValue(Integer.class);

                        if (stockActual == null) {
                            // No se pudo obtener el stock actual del producto
                            Toast.makeText(Compras.this, "Error al obtener el stock del producto", Toast.LENGTH_SHORT).show();
                        } else {
                            // Actualizar el stock en la base de datos
                            int nuevoStock = stockActual + cantidad;
                            productoRef.setValue(nuevoStock);
                            Toast.makeText(Compras.this, "Compra realizada correctamente", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Manejar el error en caso de que la lectura se cancele
                        Toast.makeText(Compras.this, "Error al obtener el stock del producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public void limpiar(){
        etCodigo.setText(null);
        tvNombre.setText(null);
        tvStock.setText(null);
        tvPrecioVenta.setText(null);
        etCantidad.setText(null);
        tvTotal.setText(null);
    }
}