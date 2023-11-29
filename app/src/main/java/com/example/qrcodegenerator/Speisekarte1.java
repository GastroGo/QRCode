package com.example.qrcodegenerator;

import java.io.Serializable;

public class Speisekarte1 implements Serializable {
    private String id, gericht, allergien, zutaten;
    private int preis;


    public Speisekarte1() {
    }

    public Speisekarte1(String id, int preis, String gericht, String allergien, String zutaten) {
        this.id = id;
        this.preis  = preis;
        this.gericht = gericht;
        this.allergien = allergien;
        this.zutaten = zutaten;
    }

    public String getId() {
        return id;
    }

    public int getPreis() {
        return preis;
    }

    public String getGericht() {
        return gericht;
    }

    public String getAllergien() {
        return allergien;
    }

    public String getZutaten() {
        return zutaten;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPreis(int preis) {
        this.preis = preis;
    }

    public void setGericht(String gericht) {
        this.gericht = gericht;
    }

    public void setZutaten(String zutaten) {
        this.zutaten = zutaten;
    }
    public void setAllergien(String allergien) {
        this.allergien = allergien;
    }

    @Override
    public String toString() {
        return "Gericht: " + gericht + '\n' +
                "Preis: " + preis + '\n' +
                "Allergien: " + allergien + '\n' +
                "Zutaten: " + zutaten + '\n';
    }
}