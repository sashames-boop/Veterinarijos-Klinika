package models;

import java.util.List;

public class Groomeris extends Vartotojas {
    protected String specializacija;
    protected List<Registracija> registracijos;

    public Groomeris(String vardas, String telefonas, String slaptazodis, String specializacija) {
        super(vardas, telefonas, slaptazodis);
        this.specializacija = specializacija;
    }

    public void priimtiPacienta(Registracija registracija) {
        System.out.println("Specialistas(e) " + vardas + " (" + specializacija + ") priima pacienta: "
                + registracija.getAugintinis().getVardas());
    }

    public void patvirtinti() {
        registracijos.get(registracijos.size() - 1).patvirtinti();
    }

    public String getSpecializacija() {
        return specializacija;
    }

    public void setSpecializacija(String specializacija) {
        this.specializacija = specializacija;
    }

    @Override
    public String toString() {
        return "Groomeris " + vardas + " (" + specializacija + ", " + telefonas + ")";
    }
}
