package com.example.qrcodegenerator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    List <String> allTableIDs = new ArrayList<>();
    List <Gericht> selectedGerichte = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        Intent intent = getIntent();
        List<Gericht> gerichtList = (List<Gericht>) getIntent().getSerializableExtra("Gerichte");

        Button btnAdd = findViewById(R.id.btnAdd);
        String idRestaurant = getIntent().getStringExtra("id");
        String idSelectedTable = getIntent().getStringExtra("idTable");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GerichtAdapter gerichtAdapter = new GerichtAdapter(gerichtList);
        recyclerView.setAdapter(gerichtAdapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrders(gerichtList);
                showDialog(selectedGerichte);
            }
        });
    }

    private void setOrders(List<Gericht> gerichtList) {
        selectedGerichte.clear();
        for (Gericht gericht : gerichtList) {
            if (gericht.isSelected() && gericht.getAmount() > 0) {
                selectedGerichte.add(gericht);
            }
        }
    }

    private void showDialog(List<Gericht> selectedGerichte) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_window);

        RecyclerView recyclerDialogView = dialog.findViewById(R.id.recyclerDialogView);

        recyclerDialogView.setLayoutManager(new LinearLayoutManager(this));
        OrderAdapter orderAdapter = new OrderAdapter(selectedGerichte);
        recyclerDialogView.setAdapter(orderAdapter);

        int dialogHeight = calculateDialogHeight(selectedGerichte);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);

        if (selectedGerichte.size() > 0) {
            dialog.show();
        } else {
            Toast.makeText(this, "Keine Gerichte zur Bestellung hinzugefügt", Toast.LENGTH_SHORT).show();
            dialog.dismiss();  // Schließe den Dialog, wenn die Liste leer ist
        }

        // Wird aufgerufen, wenn die Liste leer wird
        // Schließe den Dialog, wenn die Liste leer ist
        orderAdapter.setOnListEmptyListener(dialog::dismiss);
    }


    private int calculateDialogHeight(List<Gericht> selectedGerichte) {
        // Hier können Sie die Höhe des Dialogs basierend auf der Anzahl der Gerichte anpassen.
        // Beispiel: Hier wird die Höhe auf 400dp plus 100dp für jeden ausgewählten Artikel festgelegt.
        int itemHeight = 100;
        int minHeight = 200;
        int buttonHeigth = 200;
        int totalHeight = minHeight + (selectedGerichte.size()+2) * itemHeight;
        return Math.min(totalHeight, getResources().getDisplayMetrics().heightPixels);
    }
}

