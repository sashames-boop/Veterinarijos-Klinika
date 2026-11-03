package models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Vet extends User {
    private String specialization;
    @OneToMany(mappedBy = "vet")
    private List<Registration> registrations = new ArrayList<>();


    public Vet() {}
    public Vet(String name, String phone, String password, String specialization) {
        super(name, phone, password);
        this.specialization = specialization;
    }

    public Vet(String name, String phone, String username, String password, String specialization) {
        super(name, phone, username, password);
        this.specialization = specialization;
    }

    public void acceptPatient(Registration registration) {
        System.out.println("Vet " + name + " (" + specialization + ") accepts patient: "
                + registration.getPet().getName());
    }

    public void confirmRegistration() {
        if(registrations.isEmpty()) {
            System.out.println("Vet " + name + " (" + specialization + ") has no appointments");
            return;
        }
        registrations.get(registrations.size() - 1).confirm();
    }
    public void addRegistration(Registration registration) {
        registrations.add(registration);
    }
    public void removeRegistration(Registration registration) {
        registrations.remove(registration);
    }


    // Getteriai ir Setteriai
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}