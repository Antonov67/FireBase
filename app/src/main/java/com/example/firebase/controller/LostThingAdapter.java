package com.example.firebase.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.firebase.R;
import com.example.firebase.model.LostThing;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class LostThingAdapter extends ArrayAdapter<LostThing> {

    private List<LostThing>  lostThings;
    private Context context;
    int resource;

    public LostThingAdapter(@NonNull Context context, int resource, @NonNull List<LostThing> lostThings) {
        super(context, resource, lostThings);
        this.lostThings = lostThings;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,parent, false);
        TextView city = view.findViewById(R.id.city);
        TextView description = view.findViewById(R.id.description);
        TextView name = view.findViewById(R.id.name);
        ImageView image = view.findViewById(R.id.image);

        city.setText(lostThings.get(position).city);
        description.setText(lostThings.get(position).description);
        name.setText(lostThings.get(position).name);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(lostThings.get(position).imgPath);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("777", "Glide_url: " + uri.toString());
                Glide.with(context)
                        .load(uri)
                        .into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "ошибка получения URL", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }




}