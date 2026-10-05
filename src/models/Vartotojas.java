package models;

public class Vartotojas {
    protected String vardas;
    protected String telefonas;
    
    public Vartotojas(String vardas, String telefonas) {
        this.vardas = vardas;
        this.telefonas = telefonas;
    }
    public Vartotojas() {}
    public Vartotojas(String vardas) {}

    public String getVardas() {return vardas;}
    public void setVardas(String vardas) {this.vardas = vardas;}
    public String getTel() {return telefonas;}
    public void setTel(String telefonas) {this.telefonas = telefonas;}
}
