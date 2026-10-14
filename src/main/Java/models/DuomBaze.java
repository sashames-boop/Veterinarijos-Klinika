package models;

import java.util.ArrayList;
import java.util.List;

public class DuomBaze {
    private static final List<Klientas> klientai = new ArrayList<>();
    private static final List<Augintinis> augintiniai = new ArrayList<>();
    private static final List<Veterinaras> veterinarai = new ArrayList<>();


    public static LoginStatus patikrintiLogin(String vardas, String slaptazodis) {
        for(Klientas k: klientai) {
            if(k.getVardas().equals(vardas)) {
                if(k.getSlaptazodis().equals(slaptazodis)) {
                    return LoginStatus.SUCCESS;
                }
                if(!k.getSlaptazodis().equals(slaptazodis)) {
                    return LoginStatus.WRONG_PSW;
                }
            }
            if(!k.getVardas().equals(vardas)) {
                return LoginStatus.USER_NOT_FOUND;
            }
        }
        return LoginStatus.USER_NOT_FOUND;
    }

    public static List<Klientas> getKlientai() {
        return klientai;
    }

    public static List<Augintinis> getAugintiniai() {
        return augintiniai;
    }

    public static List<Veterinaras> getVeterinarai() { return veterinarai; }
}