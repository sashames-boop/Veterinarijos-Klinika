package models;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    protected String name;
    @Column(unique = true)
    protected String username;
    protected String phone;
    protected String password;
    private Boolean blocked = false;

    public boolean isBlocked() { return Boolean.TRUE.equals(blocked); }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public User() {}
    public User(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
    public User(String name, String phone, String username, String password) {
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }
    public User(String name) {
        this.name = name;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
