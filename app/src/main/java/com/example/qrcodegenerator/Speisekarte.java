package com.example.qrcodegenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Speisekarte {

    private String speiseName;
    private String speisePreis;
    // Weitere Eigenschaften je nach Ihren Datenbankfeldern

    public Speisekarte(String speiseName, String speisePreis) {
        this.speiseName = speiseName;
        this.speisePreis = speisePreis;
        // Initialisieren Sie andere Eigenschaften, wenn vorhanden
    }

    // Getter-Methoden für Ihre Eigenschaften

    // Methode zum Abrufen der Speisekarteninformationen aus der Datenbank
    public static void getSpeisekartenInfo(String idRestaurant, final SpeisekarteCallback callback) {
        DatabaseReference dbRestaurant = FirebaseDatabase.getInstance().getReference("Restaurants").child(idRestaurant);

        dbRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Daten gefunden, verarbeiten Sie diese
                    String speiseName = dataSnapshot.child("speiseName").getValue(String.class);
                    String speisePreis = dataSnapshot.child("speisePreis").getValue(String.class);

                    // Erstellen Sie eine Instanz der Speisekartenklasse und rufen Sie die Callback-Methode auf
                    Speisekarte speisekarte = new Speisekarte(speiseName, speisePreis);
                    callback.onSpeisekarteCallback(speisekarte);
                } else {
                    // Daten nicht gefunden
                    callback.onSpeisekarteCallback(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Fehler bei der Datenbankabfrage
                callback.onSpeisekarteCallback(null);
            }
        });
    }

    // Callback-Schnittstelle für die Rückgabe der Speisekarteninformationen
    public interface SpeisekarteCallback {
        void onSpeisekarteCallback(Speisekarte speisekarte);
    }
}
