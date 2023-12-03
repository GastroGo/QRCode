package com.example.qrcodegenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    Button buttonBack, buttonSave, buttonDelete;
    EditText editTextGericht, editTextPreis, editTextAllergien, editTextZutaten;
    Gericht selectedSpeisekarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonBack = findViewById(R.id.back_button);
        editTextGericht = findViewById(R.id.editTextGericht);
        editTextPreis = findViewById(R.id.editTextPreis);
        editTextAllergien = findViewById(R.id.editTextAllergien);
        editTextZutaten = findViewById(R.id.editTextZutaten);


        editTextGericht.setEnabled(true);
        editTextPreis.setEnabled(true);
        editTextAllergien.setEnabled(true);
        editTextZutaten.setEnabled(true);
        selectedSpeisekarte = (Gericht) getIntent().getSerializableExtra("selectedSpeisekarte");

        editTextGericht.setText(selectedSpeisekarte.getGerichtName()                                                              );
        editTextPreis.setText(String.valueOf(selectedSpeisekarte.getPreis()));


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
