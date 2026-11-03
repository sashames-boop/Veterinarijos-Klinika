package models;

import jakarta.persistence.*;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String date;
    private String time;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Pet pet;
    @ManyToOne
    private Vet vet;
    @ManyToOne
    private Groomer groomer;
    private boolean confirmed;
    private Boolean cancelled = false;

    public boolean isCancelled() { return Boolean.TRUE.equals(cancelled); }

    public Registration() {}
    public Registration(String date, String time, Client client, Pet pet, Vet vet) {
        this.date = date;
        this.time = time;
        this.client = client;
        this.pet = pet;
        this.vet = vet;
        this.confirmed = false;

        client.getRegistrations().add(this);
        pet.getRegistrations().add(this);
        vet.getRegistrations().add(this);
    }
    public Registration(String date, String time, Client client, Pet pet, Groomer groomer) {
        this.date = date;
        this.time = time;
        this.client = client;
        this.pet = pet;
        this.groomer = groomer;
        this.confirmed = false;

        client.getRegistrations().add(this);
        pet.getRegistrations().add(this);
        groomer.getRegistrations().add(this);
    }

    public void confirm() {
        if (isCancelled()) throw new IllegalStateException("Cancelled appointments cannot be confirmed.");
        this.confirmed = true;
        System.out.println("Registration at " + date + " " + time + " was CONFIRMED.");
    }

    public void cancel() {
        this.cancelled = true;
        this.confirmed = false;
        System.out.println("Registration at " + date + " " + time + " was CANCELLED.");
    }

    // Getteriai ir Setteriai
    public Integer getId() {return id;}

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Client getClient() { return client; }

    public Pet getPet() {
        return pet;
    }

    public Vet getVet() {
        return vet;
    }

    public Groomer getGroomer() { return groomer; }

    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public String toString() {
        String specialist = vet != null ? vet.toString() : groomer != null ? groomer.toString() : "Unassigned";
        return date + " " + time + " | " + specialist + " | "
                + (isCancelled() ? "cancelled" : confirmed ? "confirmed" : "pending");
    }
}