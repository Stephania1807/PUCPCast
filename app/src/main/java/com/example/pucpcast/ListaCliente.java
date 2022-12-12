package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pucpcast.DTO.Episodio;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ListaCliente extends AppCompatActivity {

    RecyclerView recyclerView;
    listaEpisodiosAdapter listaEpisodiosAdapter;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Episodio> episodios;
    SearchView searchView;
    DatabaseReference ref1;
    FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String id;

    BottomNavigationView bottomNavigationView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cliente);
        setBottomNavigationView();

        id = getIntent().getStringExtra("key");


        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        String uid = mAuth.getCurrentUser().getUid();
        searchView = findViewById(R.id.searchViewEpisodios);
//        searchView.clearFocus();
//        ref=firebaseDatabase.getReference().child(uid);
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
                    episodios.add(episodio);


                }
                recyclerView = findViewById(R.id.recyclerViewListaEpisodios);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ListaCliente.this, 2));
                recyclerView.setAdapter(listaEpisodiosAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listaEpisodiosAdapter = new listaEpisodiosAdapter(this, episodios);
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
                        listaEpisodiosAdapter = new listaEpisodiosAdapter(ListaCliente.this, episodios);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ListaCliente.this, error.getMessage(),Toast.LENGTH_SHORT).show();

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
            if(object.getTitulo().toLowerCase(Locale.ROOT).contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        listaEpisodiosAdapter listaEpisodiosAdapter = new listaEpisodiosAdapter(ListaCliente.this,mylist);
        recyclerView.setAdapter(listaEpisodiosAdapter);
    }


    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationCliente);

        bottomNavigationView.setSelectedItemId(R.id.escuchar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.escuchar:
                        return true;
                    case R.id.libreria:
                        Intent intent = new Intent(ListaCliente.this, LibreriaCliente.class);
                        intent.putExtra("key2",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.perfil:
                        Intent intent1 = new Intent(ListaCliente.this, PerfilCliente.class);
                        intent1.putExtra("key2",id);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
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
        startActivity(new Intent(ListaCliente.this, LogIn.class));}
}