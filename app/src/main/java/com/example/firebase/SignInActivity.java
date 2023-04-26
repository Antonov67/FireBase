package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    EditText email, pswrd;
    Button signInButton, regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();// разлогинимся

        email = findViewById(R.id.email_sign_in);
        pswrd = findViewById(R.id.pswrd_sign_in);
        signInButton = findViewById(R.id.sign_in_button);
        regButton = findViewById(R.id.reg_button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firebaseAuth.getCurrentUser() != null) {
                    // Already signed in
                    // Do nothing
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(SignInActivity.this, user.getEmail() +  ", вы уже вошли!", Toast.LENGTH_SHORT).show();
                    Log.d("777", "вход1");

                    //проверим на непустоту полей и корректность шаблона почты
                } else  if (isEmailValid(email.getText().toString()) && pswrd.getText().length() != 0){

                    Log.d("777", "вход2");

                   //пробуем авторизоваться

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pswrd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User signed in successfully
                                        Toast.makeText(SignInActivity.this, "вход выполнен", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInActivity.this, AddDataActivity.class));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, "ошибка входа", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



//        Map<String, Object> users = new HashMap<>();
//        users.put("name","Dima");
//        users.put("surname","Antonov");
//        users.put("position","teacher");
//
//        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(SignInActivity.this, "Ready", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignInActivity.this, "Not ready", Toast.LENGTH_SHORT).show();
//            }
//        });







    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}