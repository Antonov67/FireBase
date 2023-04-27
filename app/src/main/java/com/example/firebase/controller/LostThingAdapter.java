package com.example.firebase.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebase.R;
import com.example.firebase.ui.LostThing;

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


        city.setText(lostThings.get(position).city);
        description.setText(lostThings.get(position).description);
        name.setText(lostThings.get(position).name);

        return view;
    }
}
