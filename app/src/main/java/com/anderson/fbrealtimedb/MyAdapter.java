package com.anderson.fbrealtimedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductoLista> list;

    public MyAdapter(Context context, ArrayList<ProductoLista> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lista_pro,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductoLista lista = list.get(position);
        holder.tvCodigo.setText(lista.getCodigo());
        holder.tvNombre.setText(lista.getNomProducto());
        holder.tvStock.setText(String.valueOf(lista.getStock()));

        holder.tvVenta.setText(String.valueOf(lista.getVenta()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView tvCodigo, tvNombre, tvStock, tvVenta;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvVenta = itemView.findViewById(R.id.tvVenta);
        }
    }
}
