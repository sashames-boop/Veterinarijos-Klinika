package models;

import java.util.ArrayList;
import java.util.List;

public class Vet extends User {
    private String specialization;
    private final List<Registration> registrations;

    public Vet(String name, String specialization) {
        super(name);
        this.specialization = specialization;
        this.registrations = new ArrayList<>();
    }

    public void acceptPatient(Registration registration) {
        System.out.println("Vet " + name + " (" + specialization + ") accepts patient: "
                + registration.getPet().getName());
    }

    public void confirmRegistration() {

        registrations.get(registrations.size() - 1).confirm();
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