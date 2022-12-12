package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.pucpcast.DTO.Episodio;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaEpisodiosAdmin extends AppCompatActivity {

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
        setContentView(R.layout.activity_lista_episodios_admin);
        setBottomNavigationView();

        id = getIntent().getStringExtra("key");
    }




    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationAdministrador);

        bottomNavigationView.setSelectedItemId(R.id.episodios);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.episodios:
                        return true;
                    case R.id.perfiles:
                        Intent intent = new Intent(ListaEpisodiosAdmin.this, ListaPerfilesAdmin.class);
                        intent.putExtra("key2",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.perfil:
                        Intent intent1 = new Intent(ListaEpisodiosAdmin.this, PerfilAdministrador.class);
                        intent1.putExtra("key2",id);
                        overridePendingTransition(0,0);
                        startActivity(intent1);
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
        startActivity(new Intent(ListaEpisodiosAdmin.this, LogIn.class));}
}