package com.example.qrcodegenerator;

import java.io.Serializable;
import java.util.List;

public class Speisekarte implements Serializable {
    private String id, gericht;
    private List<String> allergien, zutaten;
    private double preis;


    public Speisekarte() {}

    public Speisekarte(String id, double preis, String gericht, List<String> allergien, List<String> zutaten) {
        this.id = id;
        this.preis = preis;
        this.gericht = gericht;
        this.allergien = allergien;
        this.zutaten = zutaten;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getGericht() {
        return gericht;
    }

    public void setGericht(String gericht) {
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