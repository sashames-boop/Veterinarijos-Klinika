package models;

public class Registracija {
    private String data;
    private String laikas;
    private Augintinis augintinis;
    private Veterinaras veterinaras;
    private boolean patvirtinta;

    public Registracija(String data, String laikas, Augintinis augintinis, Veterinaras veterinaras) {
        this.data = data;
        this.laikas = laikas;
        this.augintinis = augintinis;
        this.veterinaras = veterinaras;
        this.patvirtinta = false;

        // Pridedame augintinio registracija prie augintinio ir veterinaro istorijas
        augintinis.getRegistracijos().add(this);
        veterinaras.getRegistracijos().add(this);
    }

    public void patvirtinti() {
        this.patvirtinta = true;
        System.out.println("Registracija laiku " + data + " " + laikas + " buvo PATVIRTINTA.");
    }

    public void atsaukti() {
        this.patvirtinta = false;
        System.out.println("Registracija laiku " + data + " " + laikas + " buvo ATSAUKTA.");
    }

    // Getteriai ir Setteriai
    public String getData() {
        return data;
    }

    public String getLaikas() {
        return laikas;
    }

    public Augintinis getAugintinis() {
        return augintinis;
    }

    public Veterinaras getVeterinaras() {
        return veterinaras;
    }

    public boolean yraPatvirtinta() {
        return patvirtinta;
    }

    @Override
    public String toString() { return data + " " + laikas + " | " + veterinaras.getVardas()
        + "(" + veterinaras.getSpecializacija() + ") | " + (patvirtinta ? "patvirtinta" : "nepatvirtinta"); }
}