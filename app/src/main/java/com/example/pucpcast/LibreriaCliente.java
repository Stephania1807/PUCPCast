package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Episodio;
import com.example.pucpcast.DTO.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LibreriaCliente extends AppCompatActivity {

    RecyclerView recyclerView;
    libreriaAdapter adapter;
    ArrayList<Episodio> episodios;
    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    SearchView searchView;
    DatabaseReference ref1;
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String id;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libreria_cliente);
        setBottomNavigationView();
        id =  getIntent().getStringExtra("key2");

        searchView = findViewById(R.id.searchViewMine);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        episodios = new ArrayList<>();

        ref1  = firebaseDatabase.getReference("cliente").child("episodios");

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){

                    Episodio episodio = snapshot1.getValue(Episodio.class);

                    DatabaseReference ref = firebaseDatabase.getReference().child("usuario");

                    // OBTENER DATOS DEL USUARIO
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // SI EL USUARIO EXISTE
                            for(DataSnapshot usuarios: snapshot.getChildren()){

                                Usuario usuario = usuarios.getValue(Usuario.class);
                                listaUsuarios.add(usuario);
                                System.out.println(id);

                                for(Usuario usuario1 : listaUsuarios){
                                    if(Objects.equals(usuario1.getKey(), id)){
                                        if(episodio.getRegistrador().equals(usuario1.getNombre())){
                                            episodios.add(episodio);
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                libreriaAdapter libreriaAdapter = new libreriaAdapter(LibreriaCliente.this, episodios);
                libreriaAdapter.setEpisodios(episodios);
                libreriaAdapter.setContext(LibreriaCliente.this);
                recyclerView = findViewById(R.id.recyclerViewListaMine);
                recyclerView.setAdapter(libreriaAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(LibreriaCliente.this, 2));

                libreriaAdapter.notifyItemRangeChanged(0, episodios.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref1 != null){
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        episodios = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            episodios.add(ds.getValue(Episodio.class));
                        }
                        adapter = new libreriaAdapter(LibreriaCliente.this, episodios);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LibreriaCliente.this, error.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search (s);
                    return true;
                }
            });
        }
    }

    private void search(String str){
        ArrayList<Episodio> mylist = new ArrayList<>();
        for (Episodio object : episodios){
            if(object.getTitulo().toLowerCase(Locale.ROOT).contains(str.toLowerCase())||object.getRegistrador().toLowerCase(Locale.ROOT).contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
//        libreriaAdapter listadoEquiposUsuarioAdapter = new libreriaAdapter(LibreriaCliente.this,mylist);
//        recyclerView.setAdapter(listadoEquiposUsuarioAdapter);
    }


    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.libreria);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.libreria:
                        return true;
                    case R.id.escuchar:
                        Intent intent = new Intent(LibreriaCliente.this, ListaCliente.class);
                        intent.putExtra("key",id);
                        startActivity(intent);
                        return true;
                    case R.id.perfil:
                        Intent intent1 = new Intent(LibreriaCliente.this, PerfilCliente.class);
                        intent1.putExtra("key2",id);
                        startActivity(intent1);
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_mine,menu);
        return true;

    }


    public void accionCerrarSesionUsuarios(MenuItem item){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(LibreriaCliente.this, LogIn.class));



    }

    public void btnMenu(MenuItem item){
        Intent intent = new Intent(this, AgregarEpisodio.class);
        startActivity(intent);

    }

}