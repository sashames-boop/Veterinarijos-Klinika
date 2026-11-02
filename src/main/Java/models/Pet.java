package models;

import java.util.ArrayList;
import java.util.List;

public class Pet {
    private String name;
    private PetType type;
    private int age;
    private double weight;
    private Client owner;
    private final List<Registration> registrations;

    public Pet(String name, PetType type, int age, double weight) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this.registrations = new ArrayList<>();
    }

    public String getInfo() {
        return "Pet: " + name + " | Species: " + type
                + " | Age: " + age + " | Weight: " + weight;
    }

    //Get and Set
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }
    public void setType(PetType type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public Client getOwner() {
        return owner;
    }
    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    @Override
    public String toString() {
        return name + " [" + type + "]";
    }
}
