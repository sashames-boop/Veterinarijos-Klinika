package models;

import java.util.ArrayList;
import java.util.List;

public class Veterinaras extends Vartotojas{
    private String specializacija;
    private List<Registracija> registracijos;

    public Veterinaras(String vardas, String specializacija) {
        super(vardas);
        this.specializacija = specializacija;
        this.registracijos = new ArrayList<>();
    }

    public void priimtiPacienta(Registracija registracija) {
        System.out.println("Gydytojas " + vardas + " (" + specializacija + ") priima pacienta: "
                + registracija.getAugintinis().getVardas());
    }

    // Getteriai ir Setteriai
    public String getSpecializacija() {
        return specializacija;
    }
    public void setSpecializacija(String specializacija) { this.specializacija = specializacija; }

    public List<Registracija> getRegistracijos() {
        return registracijos;
    }

    @Override
    public String toString() { return vardas + "(" + specializacija + ")"; }
}