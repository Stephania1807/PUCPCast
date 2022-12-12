package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

public class ListaPerfilesAdmin extends AppCompatActivity {

    ArrayList<Usuario> clienteList;
    RecyclerView recyclerView;
    listaPerfilesAdapter listaPerfilesAdapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    SearchView searchView;
    DatabaseReference ref1;



    String id;

    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_perfiles_admin);
        setBottomNavigationView();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = getIntent().getStringExtra("key2");
        searchView = findViewById(R.id.searchViewPerfiles);






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

        getUsuarios();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref1 != null){
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        clienteList = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            clienteList.add(ds.getValue(Usuario.class));
                        }
                        listaPerfilesAdapter = new listaPerfilesAdapter(ListaPerfilesAdmin.this, clienteList);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ListaPerfilesAdmin.this, error.getMessage(),Toast.LENGTH_SHORT).show();

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


    public void getUsuarios(){
        clienteList = new ArrayList<>();
        firebaseDatabase.getReference().child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("cant:"+ snapshot.getChildrenCount());
                for (DataSnapshot children : snapshot.getChildren()){
                    System.out.println("cant:"+ snapshot.getChildrenCount());
                    Usuario usuario = children.getValue(Usuario.class);
                    if(usuario.getRol().equals("cliente")) {
                    clienteList.add(usuario);}
                }

                System.out.println("aqui la lista de clientes " + clienteList );




                recyclerView = findViewById(R.id.recyclerViewListaPerfiles);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ListaPerfilesAdmin.this, 2));
                recyclerView.setAdapter(listaPerfilesAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listaPerfilesAdapter = new listaPerfilesAdapter(this, clienteList);
    }

    private void search(String str){
        ArrayList<Usuario> mylist = new ArrayList<>();
        for (Usuario object : clienteList){
            if(object.getNombre().toLowerCase(Locale.ROOT).contains(str.toLowerCase())||object.getCodigo().toLowerCase(Locale.ROOT).contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        listaPerfilesAdapter listaPerfilesAdapter = new listaPerfilesAdapter(ListaPerfilesAdmin.this,mylist);
        recyclerView.setAdapter(listaPerfilesAdapter);
    }



    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationAdministrador);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.perfiles);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.perfiles:
                        return true;
                    case R.id.episodios:
                        Intent intent1 = new Intent(ListaPerfilesAdmin.this, ListaEpisodiosAdmin.class);
                        intent1.putExtra("key",id);
                        overridePendingTransition(0,0);
                        startActivity(intent1);
                        return true;
                    case R.id.perfil:
                        Intent intent = new Intent(ListaPerfilesAdmin.this, PerfilAdministrador.class);
                        intent.putExtra("key2",id);
                        overridePendingTransition(0,0);
                        startActivity(intent);
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
        startActivity(new Intent(ListaPerfilesAdmin.this, LogIn.class));}
}