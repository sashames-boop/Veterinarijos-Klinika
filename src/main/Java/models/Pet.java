package models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PetType type;
    private int age;
    private double weight;
    @ManyToOne
    private Client owner;
    @OneToMany(mappedBy = "pet")
    private List<Registration> registrations =  new ArrayList<>();


    public Pet() {}
    public Pet(String name, PetType type, int age, double weight) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
    }

    public String getInfo() {
        return "Pet: " + name + " | Species: " + type
                + " | Age: " + age + " | Weight: " + weight;
    }

    //Get and Set
    public Integer getId() { return id; }

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
