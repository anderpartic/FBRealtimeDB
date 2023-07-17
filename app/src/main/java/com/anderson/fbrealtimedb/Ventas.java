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

public class Ventas extends AppCompatActivity {

    private TextInputEditText etCodigo, tvNombre, tvStock, tvPrecioVenta, etCantidad, tvTotal;

    private Button btnBuscar, btnTotal, btnVender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
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
                    Toast.makeText(Ventas.this, "Por favor, ingresa un código", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Ventas.this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
                            limpiar();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Maneja el error en caso de que la lectura se cancele
                        Toast.makeText(Ventas.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Ventas.this, "Por favor, ingresa una cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                int cantidad = Integer.parseInt(cantidadStr);
                double precioVenta = Double.parseDouble(tvPrecioVenta.getText().toString());
                double total = cantidad * precioVenta;
                tvTotal.setText(String.valueOf(total));
            }
        });

        btnVender = findViewById(R.id.btnVender);
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidadStr = etCantidad.getText().toString();

                if (cantidadStr.isEmpty()) {
                    Toast.makeText(Ventas.this, "Por favor, ingresa una cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int cantidad = Integer.parseInt(cantidadStr);
                int stock = Integer.parseInt(tvStock.getText().toString());

                if (cantidad > stock) {
                    Toast.makeText(Ventas.this, "No hay stock suficiente", Toast.LENGTH_SHORT).show();
                } else {
                    // Realizar la venta y actualizar el stock en la base de datos
                    DatabaseReference productoRef = productosRef.child(etCodigo.getText().toString()).child("stock");
                    productoRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Integer stockActual = mutableData.getValue(Integer.class);
                            if (stockActual == null) {
                                // No se pudo obtener el stock actual del producto
                                return Transaction.abort();
                            }
                            if (stockActual < cantidad) {
                                // No hay suficiente stock disponible para la venta
                                return Transaction.abort();
                            }
                            mutableData.setValue(stockActual - cantidad);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                            if (committed) {
                                // La venta se realizó correctamente
                                Toast.makeText(Ventas.this, "Venta realizada correctamente", Toast.LENGTH_SHORT).show();
                                limpiar();
                            } else {
                                // Hubo un error al realizar la venta o no hay suficiente stock
                                Toast.makeText(Ventas.this, "No hay stock suficiente o error en la venta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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