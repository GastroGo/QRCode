package com.example.qrcodegenerator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qrcodegenerator.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final List<String> allIds = new ArrayList<>();
    List<String> allAllergien = new ArrayList<>();
    List<String> allZutaten = new ArrayList<>();


    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            checkRestaurantID(result.getContents());
        }
    });
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCamera();
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();
        readAllID();
    }

    private void readAllID() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Speisekarten");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    allIds.add(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading IDs from Firebase", databaseError.toException());
            }
        });
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);
        qrCodeLauncher.launch(options);
    }

    private void initViews() {
        binding.fab.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void checkRestaurantID(String idDatabase) {
        for (String id : allIds) {
            if (id.equals(idDatabase)) {
                getDataForId(id);
            }
        }
    }


    private void getDataForId(String id) {

        DatabaseReference dbRestaurant = FirebaseDatabase.getInstance().getReference("Speisekarten").child(id);
        dbRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Speisekarte speisekarte1 = new Speisekarte();
                    speisekarte1.setId(id);
                    speisekarte1.setGericht(dataSnapshot.child("Gericht").getValue(String.class));
                    speisekarte1.setPreis(dataSnapshot.child("Preis").getValue(Double.class));

                    showDataInToast(speisekarte1);
                    getDataAllergien(id, speisekarte1);
                    getDataZutaten(id, speisekarte1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void getDataAllergien(String id, Speisekarte speisekarte){

        DatabaseReference dbAllergien = FirebaseDatabase.getInstance().getReference("Speisekarten").child(id).child("Allergien");
        dbAllergien.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotAl : snapshot.getChildren()) {
                        String allergie = snapshotAl.getKey();
                        allAllergien.add(allergie);
                    }
                    speisekarte.setAllergien(allAllergien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataZutaten(String id, Speisekarte speisekarte){
        DatabaseReference dbZutaten = FirebaseDatabase.getInstance().getReference("Speisekarten").child(id).child("Zutaten");
        dbZutaten.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotAl : snapshot.getChildren()) {
                        String zutaten = snapshotAl.getKey();
                        allZutaten.add(zutaten);
                    }
                    speisekarte.setZutaten(allZutaten);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDataInToast(Speisekarte speisekarte) {
        Toast.makeText(this, "Gericht: " + speisekarte.getGericht(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Preis: " + speisekarte.getPreis(), Toast.LENGTH_SHORT).show();
    }
}