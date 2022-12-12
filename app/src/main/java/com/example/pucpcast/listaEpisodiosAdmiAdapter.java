package com.example.pucpcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Episodio;

import java.util.ArrayList;

public class listaEpisodiosAdmiAdapter extends RecyclerView.Adapter<listaEpisodiosAdmiAdapter.myViewHolder>{

    Context context;
    ArrayList<Episodio> list;

    public listaEpisodiosAdmiAdapter(Context context, ArrayList<Episodio> list) {
        this.context = context;
        this.list = list;

    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        Episodio episodio;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_episodios_admin, parent, false);
        return new myViewHolder(view);

    }


    @Override
    public void onBindViewHolder(listaEpisodiosAdmiAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Episodio e= list.get(position);
        holder.episodio = e;
        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        TextView categoria = holder.itemView.findViewById(R.id.categoria);
        ImageView imageView = holder.itemView.findViewById(R.id.imageEpisodio);
        titulo.setText(e.getTitulo());
        categoria.setText(String.valueOf(e.getCategoria()));
        String url = e.getUrl();

        Glide.with(holder.itemView.getContext()).load(url).into(imageView);
        String id = e.getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
