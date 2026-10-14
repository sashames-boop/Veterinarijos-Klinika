package models;

public class Vartotojas {
    private Integer id;
    protected String vardas;
    protected String telefonas;
    protected String slaptazodis;
    
    public Vartotojas(String vardas, String telefonas, String slaptazodis) {
        this.vardas = vardas;
        this.telefonas = telefonas;
        this.slaptazodis = slaptazodis;
    }
    public Vartotojas(String vardas) {
        this.vardas = vardas;
    }

    public String getVardas() {return vardas;}
    public void setVardas(String vardas) {this.vardas = vardas;}

    public String getTel() {return telefonas;}
    public void setTel(String telefonas) {this.telefonas = telefonas;}

    public String getSlaptazodis() { return slaptazodis; }
    public void setSlaptazodis(String slaptazodis) { this.slaptazodis = slaptazodis; }
}
