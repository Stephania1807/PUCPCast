package com.example.pucpcast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class listaPerfilesAdapter extends RecyclerView.Adapter<listaPerfilesAdapter.EventViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    private List<Usuario> usuariosListar;
    private Context context;

    public listaPerfilesAdapter(Context context, ArrayList<Usuario> list) {
        this.context = context;
        this.usuariosListar = list;

    }

    public List<Usuario> getUsuariosTIList() {
        return usuariosListar;
    }

    public void setusuarioList(List<Usuario> usuariosListar) {
        this.usuariosListar = usuariosListar;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public class EventViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_perfiles_admin, parent, false);
        System.out.println("aqui la lista de clientes " + usuariosListar );
        return new listaPerfilesAdapter.EventViewHolder(itemView);
    }







    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Usuario u = usuariosListar.get(position);
        ImageView imageView = holder.itemView.findViewById(R.id.imagePerfil);
        TextView nombre = holder.itemView.findViewById(R.id.nombre);
        TextView codigo = holder.itemView.findViewById(R.id.codigo);

        Button eliminar = holder.itemView.findViewById(R.id.eliminarBtn);


        //Eliminar
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eliminarkey = u.getKey();

                databaseReference.child("usuario").child(eliminarkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            storageReference.child("img/"+u.getFilename()).delete();
                            Intent intent = new Intent(getContext(),ListaPerfilesAdmin.class);

                            Toast.makeText(getContext(),"Borrado con exito para siempre",Toast.LENGTH_SHORT).show();
                            getContext().startActivity(intent);


                        }else {
                            Toast.makeText(getContext(), "Borrado fallido",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        System.out.println(u.getCodigo());

        System.out.println("imagen" + u.getFilename());

        nombre.setText(u.getNombre());
        codigo.setText(u.getCodigo());
        StorageReference imageRef = storageReference.child("img/"+u.getFilename());
        Glide.with(getContext()).load(imageRef).into(imageView);








    }

    @Override
    public int getItemCount() {
        return usuariosListar.size();
    }

}
