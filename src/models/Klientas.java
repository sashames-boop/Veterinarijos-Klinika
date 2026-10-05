package models;

import java.util.ArrayList;
import java.util.List;


public class Klientas extends Vartotojas{
    private List <Augintinis> augintiniai;
    private List <Registracija> registracijos;


    public Klientas(String vardas, String telefonas) {
        super(vardas, telefonas);
        this.augintiniai = new ArrayList<>();
        this.registracijos = new ArrayList<>();
    }

    public void uzregistruotiAugintini(Augintinis augintinis) {
        this.augintiniai.add(augintinis);
        augintinis.setKlientas(this);
        System.out.println("Augintinis " + augintinis.getVardas() + " sekmingai gautas!");
    }

    public Registracija uzsakytiPaslauga(String data, String laikas, Augintinis augintinis, Veterinaras veterinaras) {
        if(!augintiniai.contains(augintinis)) {
            System.out.println("Sis augintinis nepriklauso nurodytam vartotojui! Bandykite dar karta.");
            return null;
        }
        Registracija registracija = new Registracija(data, laikas, augintinis, veterinaras);
        registracijos.add(registracija);
        System.out.println("Paslauga augintiniui "
                + augintinis.getVardas() + " sekmingai uzsakyta!");
        return registracija;
    }

    public List<Augintinis> getAugintiniai() {
        return augintiniai;
    }
}
