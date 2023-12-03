package com.example.qrcodegenerator;

import android.content.Context;
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
    List<String> allGerichte = new ArrayList<>();
    int index;




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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");

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
                getAllGerichte(id);
            }
        }
    }

    private void getAllGerichte(String id) {
        DatabaseReference dbGerichte = FirebaseDatabase.getInstance().getReference("Restaurants").child(id).child("speisekarte");

        dbGerichte.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String gericht = snapshot.getKey();
                    allGerichte.add(gericht);
                }
                getDataGericht(id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading IDs from Firebase", databaseError.toException());
            }
        });
    }


    private void getDataGericht(String id) {

        Gericht[] gericht = new Gericht[allGerichte.size()];
        index = 0;

        for(String gerichtSelected : allGerichte) {

            DatabaseReference dbGerichte = FirebaseDatabase.getInstance().getReference("Restaurants").child(id).child("speisekarte").child(gerichtSelected);
            dbGerichte.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {

                        gericht[index] = new Gericht();  // Initialisierung eines neuen Gericht-Objekts
                        gericht[index].setGerichtName(snapshot.child("gericht").getValue(String.class));
                        gericht[index].setPreis(snapshot.child("preis").getValue(Double.class));
                        getDataAllergien(id, gericht[index], gerichtSelected);
                        getDataZutaten(id, gericht[index], gerichtSelected);


                        if (gericht[index].getGerichtName() != null) {
                            //Toast.makeText(MainActivity.this, "GerichtName: " + gericht[index].getGerichtName(), Toast.LENGTH_SHORT).show();
                        }

                        if (gericht[index].getPreis() != null) {
                            //Toast.makeText(MainActivity.this, "GerichtPreis: " + gericht[index].getPreis().toString(), Toast.LENGTH_SHORT).show();
                        }

                        index++;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void getDataAllergien(String id, Gericht gericht, String gerichtSelected){

        allAllergien.clear();
        DatabaseReference dbAllergien = FirebaseDatabase.getInstance().getReference("Restaurants").child(id).child("speisekarte").child(gerichtSelected).child("allergien");
        dbAllergien.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotAl : snapshot.getChildren()) {
                        String allergie = snapshotAl.getKey();
                        boolean allergieValue = snapshotAl.getValue(Boolean.class);

                        if (allergieValue) {
                            allAllergien.add(allergie);
                            //Toast.makeText(MainActivity.this, "Allergie: " + allergie, Toast.LENGTH_SHORT).show();
                        }
                    }
                    gericht.setAllergien(allAllergien);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getDataZutaten(String id, Gericht gericht, String gerichtSelceted){

        allZutaten.clear();
        DatabaseReference dbZutaten = FirebaseDatabase.getInstance().getReference("Restaurants").child(id).child("speisekarte").child(gerichtSelceted).child("zutaten");
        dbZutaten.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotAl : snapshot.getChildren()) {
                        String zutaten = snapshotAl.getKey();
                        boolean zutatenValue = snapshotAl.getValue(Boolean.class);

                        if (zutatenValue) {
                            allZutaten.add(zutaten);
                            Toast.makeText(MainActivity.this, "Zutaten: " + zutaten, Toast.LENGTH_SHORT).show();
                        }
                    }
                    gericht.setZutaten(allZutaten);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}