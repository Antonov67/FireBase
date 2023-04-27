package com.example.firebase.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    EditText email, pswrd;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();// разлогинимся

        email = findViewById(R.id.sign_up_email);
        pswrd = findViewById(R.id.sign_up_pswrd);
        signUpButton = findViewById(R.id.sign_up_button);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // Already signed in
                    // Do nothing
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(SignUpActivity.this, "user: " + user.getEmail() +  "вы уже вошли!", Toast.LENGTH_SHORT).show();
                    Log.d("777", "вход1");

                    //проверим на непустоту полей и корректность шаблона почты
                } else  if (isEmailValid(email.getText().toString()) && pswrd.getText().length() >= 6){

                    Log.d("777", "вход2");

                    //пробуем авторизоваться

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pswrd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User signed in successfully
                                        Toast.makeText(SignUpActivity.this, "такой юзер уже есть", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pswrd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "user add", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                }else {
                    Toast.makeText(SignUpActivity.this, "ошибка при вводе почты или пароля, пароль не менее 6 знаков", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}