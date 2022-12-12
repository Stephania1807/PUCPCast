package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Episodio;
import com.example.pucpcast.DTO.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AgregarEpisodio extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference ref;
    TextInputLayout tituloInputLayout, descripcionInputLayout;

    Button subirFoto, agregarbtn;
    ImageView imageView;
    ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    FirebaseUser user;
    FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

    Episodio episodio = new Episodio();

    String id;
    StorageReference storageReference;
    String nombre;

    private static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_episodio);
        id =  getIntent().getStringExtra("key2");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        tituloInputLayout = findViewById(R.id.tituloInputLayout);
        descripcionInputLayout = findViewById(R.id.descripcionInputLayout);




        storageReference = FirebaseStorage.getInstance().getReference();
        subirFoto = findViewById(R.id.subirImagenBtn);
        imageView = findViewById(R.id.imageMineGrande);


        subirFoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri = data.getData();


            StorageReference filepath = storageReference.child("fotos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    if (uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference ref = firebaseDatabase.getReference();
                                DatabaseReference refequipos = ref.child("cliente").child("episodios");

                                String id2 = refequipos.push().getKey();

                                String download_uri=uri.toString();

                                Toast.makeText(AgregarEpisodio.this, "Se subio correctamente la foto", Toast.LENGTH_SHORT).show();
                                episodio.setUrl(download_uri);

                                refequipos.child(id2).setValue(episodio).addOnSuccessListener(unused -> {
                                    Glide.with(AgregarEpisodio.this).load(download_uri).into(imageView);
                                });
                            }
                        });
                    }
                }
            });
        }

    }

    public void guardarEpisodio(View view) {

        agregarbtn = findViewById(R.id.agregarBtn);
        ref = firebaseDatabase.getReference().child("usuario");
        System.out.println(user.getUid());
        // OBTENER DATOS DEL USUARIO
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // SI EL USUARIO EXISTE
                for(DataSnapshot usuarios: snapshot.getChildren()){

                    Usuario usuario = usuarios.getValue(Usuario.class);
                    listaUsuarios.add(usuario);
                    System.out.println(id);
                    System.out.println("holiwis");

                    for(Usuario usuario1 : listaUsuarios){
                        if(Objects.equals(usuario1.getKey(), id)){
                            System.out.println("entra quiiiiiii");
                            System.out.println(id);

                            agregarbtn.setOnClickListener((view) ->{
                                HashMap episodio = new HashMap();
                                episodio.put("id", id);
                                episodio.put("titulo", tituloInputLayout.getEditText().getText().toString().trim());
                                episodio.put("descripcion", descripcionInputLayout.getEditText().getText().toString().trim());
                                episodio.put("registrador", usuario1.getNombre());

                                firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference ref1  = firebaseDatabase.getReference().child("cliente").child("episodios");

                                ref1.push().setValue(episodio);
                                Toast.makeText(AgregarEpisodio.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(AgregarEpisodio.this, LibreriaCliente.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("key2", id);
                                startActivity(intent2);

                            });

                        }
                        System.out.println("esta es el id: "+id);
                        System.out.println("este es la key del usuario: "+usuario1.getKey());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void cancelar(View view){
        Intent intent = new Intent(AgregarEpisodio.this, LibreriaCliente.class);
        intent.putExtra("key2",id);
        overridePendingTransition(0,0);
        startActivity(intent);
    }
}