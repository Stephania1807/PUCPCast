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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class libreriaAdapter extends RecyclerView.Adapter<libreriaAdapter.myViewHolder>{

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    private List<Episodio> episodios;
    private Context context;

    public libreriaAdapter(Context context, ArrayList<Episodio> episodios) {
        this.context = context;
        this.episodios = episodios;

    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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
    public libreriaAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_mine, parent, false);
        return new myViewHolder(view);

    }


    @Override
    public void onBindViewHolder(myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Episodio e= episodios.get(position);
        holder.episodio = e;
        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        TextView registrador = holder.itemView.findViewById(R.id.registrador);
        ImageView imageView = holder.itemView.findViewById(R.id.imageMine);
        titulo.setText(e.getTitulo());
        System.out.println(e.getTitulo());
        System.out.println(e.getRegistrador());
        registrador.setText(e.getRegistrador());
        String url = e.getUrl();

        Glide.with(holder.itemView.getContext()).load(url).into(imageView);
        String id = e.getId();


        Button btn_detalles = holder.itemView.findViewById(R.id.verDetalleBtn);
        Button eliminar = holder.itemView.findViewById(R.id.eliminarBtn);
        btn_detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), MineGrande.class);
                intent.putExtra("iddetalle", id);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        //Eliminar
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eliminarkey = e.getId();

                databaseReference.child("cliente").child("episodios").child(eliminarkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            storageReference.child("img/"+e.getUrl()).delete();
                            Intent intent = new Intent(getContext(), LibreriaCliente.class);

                            Toast.makeText(getContext(),"Borrado con exito para siempre",Toast.LENGTH_SHORT).show();
                            getContext().startActivity(intent);


                        }else {
                            Toast.makeText(getContext(), "Borrado fallido",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


    }


    @Override
    public int getItemCount() {
        return episodios.size();
    }

}
