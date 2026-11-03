package models;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {}

    public Admin(String name, String phone, String username, String password) {
        super(name, phone, username, password);
    }

    @Override
    public String toString() {
        return "Admin " + getName() + " (" + getPhone() + ", " + getUsername() + ")";
    }
}
