package models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends User {
    private String email;

    @OneToMany(mappedBy = "owner")
    private List <Pet> pets =  new ArrayList<>();

    @OneToMany(mappedBy = "client")
    private List <Registration> registrations =  new ArrayList<>();


    public Client() {}
    public Client(String name, String phone, String email) {
        super(name, phone, "");
        this.email = email;
    }

    public Client(String name, String tel, String email, String username, String password) {
        super(name, tel, username, password);
        this.email = email;
    }

    public void registerPet(Pet pet) {
        if (!pets.contains(pet)) {
            pets.add(pet);
        }
        pet.setOwner(this);
        System.out.println("Pet " + pet.getName() + " registered successfully!");
    }

    public Registration bookAppointment(String date, String time, Pet pet, Vet vet) {
        if (!pets.contains(pet)) {
            System.out.println("This pet does not belong to the specified client. Please try again.");
            return null;
        }

        Registration registration = new Registration(date, time, this, pet, vet);
        System.out.println("Appointment for pet " + pet.getName() + " booked successfully!");
        return registration;
    }


    //Geteriai ir Setteriai
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Pet> getPets() { return pets; }

    public List<Registration> getRegistrations() { return registrations; }

    @Override
    public String toString() { return name + " (" + phone + ")"; }

}
