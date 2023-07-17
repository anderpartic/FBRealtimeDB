package com.anderson.fbrealtimedb;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductoLista {
    private String codigo;
    private String nomProducto;
    private int stock;
    private double venta;

    public ProductoLista() {
    }

    public ProductoLista(String codigo, String nomProducto, int stock, double venta) {
        this.codigo = codigo;
        this.nomProducto = nomProducto;
        this.stock = stock;
        this.venta = venta;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public int getStock() {
        return stock;
    }

    public double getVenta() {
        return venta;
    }
}
