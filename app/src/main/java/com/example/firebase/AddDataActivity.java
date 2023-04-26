package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDataActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    EditText city, description, name;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        addButton = findViewById(R.id.add_button);

        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("data");

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

    public void click(View view) {
//        reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("777", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("777","get"+ String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                HashMap<String, LostThing> hashMap = (HashMap<String, LostThing>) snapshot.child(user.getUid()).getValue();
//                Log.d("777", hashMap.keySet().toString());
//                for (Map.Entry entry: hashMap.entrySet()) {
//                    Log.d("777",entry.getValue() + "");
//                }
                for (DataSnapshot ds : snapshot.child(user.getUid()).getChildren()) {
                    LostThing lostThing = ds.getValue(LostThing.class);
                    Log.d("777", lostThing.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}