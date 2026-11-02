package models;

public class Registration {
    private final String date;
    private final String time;
    private final Pet pet;
    private final Vet vet;
    private boolean confirmed;

    public Registration(String date, String time, Pet pet, Vet vet) {
        this.date = date;
        this.time = time;
        this.pet = pet;
        this.vet = vet;
        this.confirmed = false;

        // Keep the registration in both the pet and vet history.
        pet.getRegistrations().add(this);
        vet.getRegistrations().add(this);
    }

    public void confirm() {
        this.confirmed = true;
        System.out.println("Registration at " + date + " " + time + " was CONFIRMED.");
    }

    public void cancel() {
        this.confirmed = false;
        System.out.println("Registration at " + date + " " + time + " was CANCELLED.");
    }

    // Getteriai ir Setteriai
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Pet getPet() {
        return pet;
    }

    public Vet getVet() {
        return vet;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public String toString() {
        return date + " " + time + " | " + vet.getName()
                + " (" + vet.getSpecialization() + ") | "
                + (confirmed ? "confirmed" : "not confirmed");
    }
}