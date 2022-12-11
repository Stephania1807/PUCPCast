package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pucpcast.DTO.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class PerfilCliente extends AppCompatActivity {

    ImageView imageView;
    TextView uid, tv_nombre_edit, tv_correo_edit, tv_codigo_edit;
    Button btn_editarfoto, btn_eliminar;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference ref;

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    ArrayList<String> listallaves = new ArrayList<>();
    StorageReference storageReference;
    String id;

    private static final String TAG = "PerfilCliente";

    BottomNavigationView bottomNavigationView;
    private static final int GALLERY_INTENT = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        setBottomNavigationView();

        imageView = findViewById(R.id.imageView);
        tv_nombre_edit = findViewById(R.id.editNombre);
        tv_correo_edit = findViewById(R.id.editCorreo);
        tv_codigo_edit = findViewById(R.id.editCodigo);

        btn_editarfoto = findViewById(R.id.btn_editarfoto);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        id =  getIntent().getStringExtra("key2");


/*



        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuario").child(user.getUid());

 */

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

                    for(Usuario usuario1 : listaUsuarios){
                        if(Objects.equals(usuario1.getKey(), id)){
                            System.out.println("entra quiiiiiii");
                            System.out.println(id);
                            String nombre = usuario1.getNombre();
                            String codigo = usuario1.getCodigo();
                            String correo = usuario1.getCorreo();
                            String url = usuario1.getUrl();
                            tv_correo_edit.setText(correo);
                            tv_codigo_edit.setText(codigo);
                            tv_nombre_edit.setText(nombre);
                            Glide.with(PerfilCliente.this).load(url).into(imageView);
                        }
                    }

                }
          /*      if(snapshot.getKey().equals()){
                    //OBTENEMOS LOS DATOS DE FIREBASE
//                    String nombres = ""+snapshot.child("Nombre").getValue();
                    String codigo = ""+snapshot.child("codigo").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String image = ""+snapshot.child("imagen").getValue();

                    //SETEAMOS LOS DATOS EN LOS TEXTVIEW E IMAGEVIEW
//                    tv_nombre_edit.setText(nombres);
                    tv_correo_edit.setText(correo);
                    tv_codigo_edit.setText(codigo);
                    System.out.println(codigo);
                    System.out.println(correo);

                    //OBTENEMOS IMAGEN

                        Glide.with(PerfilUsuarioTI.this).load(image).into(imageView);


           */


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        btn_editarfoto = findViewById(R.id.btn_editarfoto);


        btn_editarfoto.setOnClickListener(view -> {
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


            StorageReference filepath = storageReference.child("perfil").child(uri.getLastPathSegment());
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
                                DatabaseReference perfil = ref.child("usuario").child(id);

                                String download_uri=uri.toString();
                                HashMap usuario = new HashMap();
                                usuario.put("url", download_uri);
                                Glide.with(PerfilCliente.this).load(download_uri).into(imageView);
                                perfil.updateChildren(usuario).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(PerfilCliente.this, "Se subio correctamente la foto", Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }
                        });
                    }
                }
            });
        }

    }

    //----------------------------------------------------------------------------------------------

//    public void checkCurrentUser() {
//        // [START check_current_user]
//        if (user != null) {
//            // User is signed in
//        } else {
//            // No user is signed in
//        }
//        // [END check_current_user]
//    }
//
//    public void getUserProfile() {
//        // [START get_user_profile]
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//        }
//        // [END get_user_profile]
//    }
//
//    public void getProviderData() {
//        // [START get_provider_data]
//        if (user != null) {
//            for (UserInfo profile : user.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                String uid = profile.getUid();
//
//                // Name, email address, and profile photo Url
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                Uri photoUrl = profile.getPhotoUrl();
//            }
//        }
//        // [END get_provider_data]
//    }
//
//    public void updateProfile() {
//        // [START update_profile]
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName("Jane Q. User")
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User profile updated.");
//                        }
//                    }
//                });
//        // [END update_profile]
//    }

    //----------------------------------------------------------------------------------------------
    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.perfil);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.perfil:
                        return true;
                    case R.id.escuchar:
                        startActivity(new Intent(PerfilCliente.this, ListaCliente.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.libreria:
                        startActivity(new Intent(PerfilCliente.this, LibreriaCliente.class));
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
        startActivity(new Intent(PerfilCliente.this, LogIn.class));}
}