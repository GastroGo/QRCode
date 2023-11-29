package com.example.qrcodegenerator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qrcodegenerator.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<String> allIds = new ArrayList<>();

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCamera();
                } else {
                    // Handle the case where camera permission is not granted
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Durchlaufe alle Kinder unter "Restaurants"
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

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            checkRestaurantID(result.getContents());
        }
    });

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR Code");
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
        ) == PackageManager.PERMISSION_GRANTED){
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
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            //id = id.substring(1);
            if (id.equals(idDatabase)) {
                getDataForId(id);
            }
        }

        // If no match is found
        Toast.makeText(this, "No matching ID found", Toast.LENGTH_SHORT).show();
    }

    private void getDataForId(String id) {
        DatabaseReference dbRestaurant = FirebaseDatabase.getInstance().getReference("Speisekarten").child(id);

        dbRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Speisekarte1 speisekarte1 = new Speisekarte1();
                    speisekarte1.setId(id);
                    speisekarte1.setGericht(dataSnapshot.child("gericht").getValue(String.class));
                    speisekarte1.setAllergien(dataSnapshot.child("allergien").getValue(String.class));
                    speisekarte1.setZutaten(dataSnapshot.child("zutaten").getValue(String.class));

                    Integer preis = dataSnapshot.child("preis").getValue(Integer.class);
                    if (preis != null) {
                        speisekarte1.setPreis(preis);
                    } else {
                        // Handle the case where the price is null
                    }

                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("selectedRestaurant", speisekarte1);
                    startActivity(intent);
                } else {
                    // Handle the case where the data does not exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the case where the read operation failed
            }
        });
    }
}

