package com.example.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    Button buttonBack, buttonSave, buttonDelete;
    EditText editTextGericht, editTextPreis, editTextAllergien, editTextZutaten;
    Speisekarte1 selectedSpeisekarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonBack = findViewById(R.id.back_button);
        buttonSave = findViewById(R.id.save_button);
        buttonDelete = findViewById(R.id.delete_button);
        editTextGericht = findViewById(R.id.editTextGericht);
        editTextPreis = findViewById(R.id.editTextPreis);
        editTextAllergien = findViewById(R.id.editTextAllergien);
        editTextZutaten = findViewById(R.id.editTextZutaten);


        editTextGericht.setEnabled(true);
        editTextPreis.setEnabled(true);
        editTextAllergien.setEnabled(true);
        editTextZutaten.setEnabled(true);
        selectedSpeisekarte = (Speisekarte1) getIntent().getSerializableExtra("selectedSpeisekarte");

        editTextGericht.setText(selectedSpeisekarte.getGericht());
        editTextPreis.setText(String.valueOf(selectedSpeisekarte.getPreis()));
        editTextAllergien.setText(selectedSpeisekarte.getAllergien());
        editTextZutaten.setText(selectedSpeisekarte.getZutaten());

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
}
}
