package com.anderson.fbrealtimedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Inventario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Manejar los clics de las opciones del menú
        if (id == R.id.venta_option) {
            Intent i = new Intent(Inventario.this, Ventas.class);
            startActivity(i);
            return true;
        } else if (id == R.id.compra_option) {
            Intent i = new Intent(Inventario.this, Compras.class);
            startActivity(i);
            return true;
        } else if (id == R.id.lista_option) {
            Intent i = new Intent(Inventario.this, ListaProducto.class);
            startActivity(i);

        } else if (id == R.id.cerrar_sesion_option) {
            Intent i = new Intent(Inventario.this, MainActivity.class);
            startActivity(i);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}