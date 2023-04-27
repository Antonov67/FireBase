package com.example.firebase.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebase.R;
import com.example.firebase.model.LostThing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddDataActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText city, description, name;
    Button addButton, showDataButton, chooseFotoButton;
    ImageView image;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        addButton = findViewById(R.id.add_button);
        showDataButton = findViewById(R.id.to_show_data_button);
        chooseFotoButton = findViewById(R.id.choose_button);
        image = findViewById(R.id.choose_image);

        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("data");

        //выбор фото из галлереи
        chooseFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

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

                //генерим ссылку для фото
                StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

                LostThing lostThing = new LostThing(
                        city.getText().toString(),
                        description.getText().toString(),
                        name.getText().toString(),
                        user.getEmail(),
                        ref.toString());

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

                addLostThing(lostThing, ref);
            }
        });



    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addLostThing(LostThing lostThing, StorageReference ref) {

        //добавим потеряшку
        String id = reference.push().getKey();
        reference.child(user.getUid()).child(id).setValue(lostThing);

        //добавим фото
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDataActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            city.setText("");
                            name.setText("");
                            description.setText("");
                            image.setImageResource(0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDataActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}