package models;

import java.util.ArrayList;
import java.util.List;

public class Augintinis {
    private String vardas;
    private String rusis;
    private int amzius;
    private double svoris;
    private Klientas klientas;
    private List<Registracija> registracijos;

    public Augintinis(String vardas, String rusis, int amzius, double svoris) {
        this.vardas = vardas;
        this.rusis = rusis;
        this.amzius = amzius;
        this.registracijos = new ArrayList<>();
        this.svoris = svoris;
    }

    public String gautiInfo() {
        return "models.Augintinis: " + vardas + " | Rusis: " + rusis +
                " | Amzius: " + amzius + " | Svoris: " + svoris;
    }

    //Get and Set
    public String getVardas() {
        return vardas;
    }

    public String getRusis() {
        return rusis;
    }

    public int getAmzius() {
        return amzius;
    }

    public Klientas getKlientas() {
        return klientas;
    }

    public void setKlientas(Klientas klientas) {
        this.klientas = klientas;
    }

    public List<Registracija> getRegistracijos() {return registracijos;}

    public double getSvoris() {
        return svoris;
    }

    public void setSvoris(double svoris) {
        this.svoris = svoris;
    }
}
