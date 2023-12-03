package com.example.qrcodegenerator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        Intent intent = getIntent();
        List<Gericht> gerichtList = (List<Gericht>) getIntent().getSerializableExtra("Gerichte");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GerichtAdapter gerichtAdapter = new GerichtAdapter(gerichtList);
        recyclerView.setAdapter(gerichtAdapter);
    }
}
