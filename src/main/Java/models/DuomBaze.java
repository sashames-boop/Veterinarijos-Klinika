package models;

import java.util.ArrayList;
import java.util.List;

public class DuomBaze {
    private static final List<Klientas> klientai = new ArrayList<>();
    private static final List<Augintinis> augintiniai = new ArrayList<>();
    private static final List<Veterinaras> veterinarai = new ArrayList<>();

    public static List<Klientas> getKlientai() {
        return klientai;
    }

    public static List<Augintinis> getAugintiniai() {
        return augintiniai;
    }

    public static List<Veterinaras> getVeterinarai() { return veterinarai; }
}