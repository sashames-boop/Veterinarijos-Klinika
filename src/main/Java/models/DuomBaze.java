package models;

import java.util.ArrayList;
import java.util.List;

public class DuomBaze {
    private static final List<Client> clients = new ArrayList<>();
    private static final List<Pet> pets = new ArrayList<>();
    private static final List<Vet> vets = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();


    public static LoginStatus validateLogin(String username, String password) {
        for(User u: users) {
            if(username.equals(u.getUsername())) {
                if(password.equals(u.getPassword())) {
                    return LoginStatus.SUCCESS;
                }
                return LoginStatus.WRONG_PASSWORD;
            }
        }
        return LoginStatus.USER_NOT_FOUND;
    }

    public static User getUserByCredentials(String username, String password) {

        for (User user : users) {

            if (username.equals(user.getUsername())
                    && password.equals(user.getPassword())) {

                return user;
            }
        }

        return null;
    }

    public static List<Client> getClients() {
        return clients;
    }

    public static List<Pet> getPets() {
        return pets;
    }

    public static List<Vet> getVets() {
        return vets;
    }

    public static List<User> getUsers() { return users; }
}