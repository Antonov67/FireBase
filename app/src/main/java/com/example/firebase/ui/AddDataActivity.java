package com.example.firebase.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDataActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    EditText city, description, name;
    Button addButton, showDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        addButton = findViewById(R.id.add_button);
        showDataButton = findViewById(R.id.to_show_data_button);

        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("data");

        showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDataActivity.this, ShowDataActivity.class));
            }
        });

        //добавим запись
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddDataActivity.this, "add start", Toast.LENGTH_SHORT).show();
                 LostThing lostThing = new LostThing(
                        city.getText().toString(),
                        description.getText().toString(),
                        name.getText().toString(),
                        user.getEmail());

                 //добавление данных, если база данных НЕ realtime
//                 firestore.collection("data").add(lostThing).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                     @Override
//                     public void onSuccess(DocumentReference documentReference) {
//                         Toast.makeText(AddDataActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
//                     }
//                 }).addOnFailureListener(new OnFailureListener() {
//                     @Override
//                     public void onFailure(@NonNull Exception e) {
//                         Toast.makeText(AddDataActivity.this, "Не добавлено", Toast.LENGTH_SHORT).show();
//                     }
//                 });

                //добавим данные в БД  - realtime Database
                String id = reference.push().getKey();
                reference.child(user.getUid()).child(id).setValue(lostThing);
            }
        });



    }


}