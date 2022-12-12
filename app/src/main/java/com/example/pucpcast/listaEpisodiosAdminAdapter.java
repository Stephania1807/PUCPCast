package com.example.pucpcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Episodio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class listaEpisodiosAdminAdapter extends RecyclerView.Adapter<listaEpisodiosAdminAdapter.myViewHolder>{

    Context context;
    ArrayList<Episodio> episodios;
    FirebaseDatabase firebaseDatabase;

    public listaEpisodiosAdminAdapter(Context context, ArrayList<Episodio> list) {
        this.context = context;
        this.episodios = list;

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
    public void onBindViewHolder(listaEpisodiosAdminAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Episodio e= episodios.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref1  = firebaseDatabase.getReference("cliente").child("episodios");
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


        Button btn_eliminar = holder.itemView.findViewById(R.id.eliminarBtn);
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(titulo.getContext());
                builder.setTitle("¿Estas seguro?");
                builder.setMessage("No puedes deshacer esta accion");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ref1.child(id).removeValue();
                        Intent intent2 = new Intent(holder.itemView.getContext(), ListaEpisodiosAdmin.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        holder.itemView.getContext().startActivity(intent2);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(titulo.getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodios.size();
    }
}
