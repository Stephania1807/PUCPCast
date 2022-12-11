package com.example.pucpcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    TextInputLayout correo;
    Button btnCambiarContra;
    private String email = "" ;

    private ProgressDialog mDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        correo = findViewById(R.id.correoInputLayout);
        btnCambiarContra = findViewById(R.id.btn_restablecer);

        mDialog = new ProgressDialog(this);

        btnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = correo.getEditText().getText().toString();

                if(!email.isEmpty()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }
                else{
                    Toast.makeText( ForgetPassword.this, "Debe ingresar su correo", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();


            }
        });



    }

    private void resetPassword(){

        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText( ForgetPassword.this, "Se ha enviado un correo para reestablecer tu contraseña", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText( ForgetPassword.this, "No se pudo enviar el correo de reestablecer contraseña", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    public void volverLogin(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

}