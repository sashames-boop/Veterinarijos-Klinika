package models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Groomer extends User {
    protected String specialization;
    @OneToMany(mappedBy = "groomer")
    private List<Registration> registrations =  new ArrayList<>();


    public Groomer() {}
    public Groomer(String name, String phone, String password, String specialization) {
        super(name, phone, password);
        this.specialization = specialization;
    }

    public Groomer(String name, String phone, String username, String password, String specialization) {
        super(name, phone, username, password);
        this.specialization = specialization;
    }

    public void acceptPatient(Registration registration) {
        System.out.println("Groomer " + name + " (" + specialization + ") accepts patient: "
                + registration.getPet().getName());
    }

    public void confirmRegistration() {
        if(registrations.isEmpty()) {
            System.out.println("Groomer " + name + " (" + specialization + ") has no appointments");
            return;
        }
        registrations.get(registrations.size() - 1).confirm();
    }


    public List<Registration> getRegistrations() {
        return registrations;
    }

    //Geteriai ir Setteriai
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Groomer " + name + " (" + specialization + ", " + phone + ")";
    }
}
