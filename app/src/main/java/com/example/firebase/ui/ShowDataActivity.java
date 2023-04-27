package com.example.firebase.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.R;
import com.example.firebase.controller.LostThingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowDataActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    LostThingAdapter adapter;

    TextView userIdText;
    ListView listView;
    Button allRecButton, recByUserButton;
    List<LostThing> lostThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        listView = findViewById(R.id.list);
        userIdText = findViewById(R.id.text_user_id);
        allRecButton = findViewById(R.id.all_rec_button);
        recByUserButton = findViewById(R.id.rec_by_user_button);

        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("data");


        lostThings = new ArrayList<>();
        adapter = new LostThingAdapter(ShowDataActivity.this, R.layout.item_layout, lostThings);
        listView.setAdapter(adapter);





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

        allRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       //получим все данные по всем юзерам
                        HashMap<String,LostThing> hashMap = (HashMap<String, LostThing>) snapshot.getValue();
                        // Log.d("777", hashMap.keySet().toString());
                        lostThings.clear();
                        for (String s : hashMap.keySet()){
                            for (DataSnapshot ds : snapshot.child(s).getChildren()) {
                                LostThing lostThing = ds.getValue(LostThing.class);
                                lostThings.add(lostThing);
                                Log.d("777", "allRec: " + lostThing.toString());
                            }
                        }
                        userIdText.setText("все записи");
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowDataActivity.this, "Данные не получаются(((", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        recByUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //  в цикле получим все данные текущего юзера
                        lostThings.clear();
                        for (DataSnapshot ds : snapshot.child(user.getUid()).getChildren()) {
                            LostThing lostThing = ds.getValue(LostThing.class);
                            lostThings.add(lostThing);
                            Log.d("777", lostThing.toString());
                        }

                        userIdText.setText(user.getEmail() + "\n" + user.getUid());

                       adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowDataActivity.this, "Данные не получаются(((", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}