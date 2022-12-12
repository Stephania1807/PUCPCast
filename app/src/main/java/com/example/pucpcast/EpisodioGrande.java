package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pucpcast.DTO.Episodio;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EpisodioGrande extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    TextView titulo, registrador, descripcion;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodio_grande);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cliente").child("episodios");

        id =  getIntent().getStringExtra("iddetalle");
        System.out.println(id);
        titulo = findViewById(R.id.tituloGrande);
        registrador = findViewById(R.id.registradorEdit);
        descripcion = findViewById(R.id.descripcionGrande);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Episodio> episodios = new ArrayList<>();
                if (snapshot.exists()) {

                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Episodio episodio = snapshot1.getValue(Episodio.class);
                        episodios.add(episodio);
                    }

                    for(Episodio episodio: episodios){

                        if(Objects.equals(episodio.getId(), id)){
                            titulo.setText(episodio.getTitulo());
                            registrador.setText(episodio.getRegistrador());
                            descripcion.setText(episodio.getDescripcion());

                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return true;

    }


    public void accionCerrarSesionUsuarios(MenuItem item){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(EpisodioGrande.this, LogIn.class));



    }
}