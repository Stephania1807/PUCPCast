package com.example.pucpcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Episodio;

import java.util.ArrayList;

public class listaEpisodiosAdapter extends RecyclerView.Adapter<listaEpisodiosAdapter.myViewHolder>{
    Context context;
    ArrayList<Episodio> episodios;

    public listaEpisodiosAdapter(Context context, ArrayList<Episodio> episodios) {
        this.context = context;
        this.episodios = episodios;

    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView titulo, registrador;

        Episodio episodio;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_episodios, parent, false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Episodio e= episodios.get(position);
        holder.episodio = e;
        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        TextView registrador = holder.itemView.findViewById(R.id.registradorCliente);
        ImageView imageView = holder.itemView.findViewById(R.id.imageEpisodio);
        titulo.setText(e.getTitulo());
        System.out.println(e.getTitulo());
        System.out.println(e.getRegistrador());
        registrador.setText(e.getRegistrador());
        String url = e.getUrl();

        Glide.with(holder.itemView.getContext()).load(url).into(imageView);
        String id = e.getId();


        Button btn_detalles = holder.itemView.findViewById(R.id.verDetalleBtn);
        btn_detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), EpisodioGrande.class);
                intent.putExtra("iddetalle", id);
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return episodios.size();
    }
}
