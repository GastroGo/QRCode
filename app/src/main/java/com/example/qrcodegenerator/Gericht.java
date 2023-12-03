package com.example.qrcodegenerator;

import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class Gericht implements Serializable {
    private String id, gericht;
    private List<String> allergien, zutaten;
    private Double preis;


    public Gericht() {}

    public Gericht(String id, Double preis, String gericht, List<String> allergien, List<String> zutaten) {
        this.id = id;
        this.preis = preis;
        this.gericht = gericht;
        this.allergien = allergien;
        this.zutaten = zutaten;
    }

    public Double getPreis() {
        return preis;
    }

    public void setPreis(Double preis) {
        this.preis = preis;
    }

    public String getGerichtName() {
        return gericht;
    }

    public void setGerichtName(String gericht) {
        this.gericht = gericht;
    }

    public List<String> getAllergien() {
        return allergien;
    }

    public void setAllergien(List<String> allergien) {
        this.allergien = allergien;
    }

    public List<String> getZutaten() {
        return zutaten;
    }

    public void setZutaten(List<String> zutaten) {
        this.zutaten = zutaten;
    }



    @Override
    public String toString() {
        return "Gericht: " + gericht + '\n' +
                "Preis: " + preis + '\n' +
                "Allergien: " + allergien + '\n' +
                "Zutaten: " + zutaten + '\n';
    }
}