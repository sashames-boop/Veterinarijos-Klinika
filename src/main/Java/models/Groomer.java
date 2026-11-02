package models;

import java.util.ArrayList;
import java.util.List;

public class Groomer extends User {
    protected String specialization;
    protected final List<Registration> registrations;

    public Groomer(String name, String phone, String password, String specialization) {
        super(name, phone, password);
        this.specialization = specialization;
        this.registrations = new ArrayList<>();
    }

    public void acceptPatient(Registration registration) {
        System.out.println("Groomer " + name + " (" + specialization + ") accepts patient: "
                + registration.getPet().getName());
    }

    public void confirmLatestRegistration() {
        registrations.get(registrations.size() - 1).confirm();
    }

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
