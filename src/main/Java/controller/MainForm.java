package controller;

import javafx.fxml.FXML;
import models.Admin;

public class MainForm {
    @FXML
    private UsersController usersController;
    @FXML
    private PetsController petsController;
    @FXML
    private RegistrationsController registrationsController;
    @FXML
    private IncidentsController incidentsController;

    public void setAdmin(Admin admin) {
        usersController.setAdmin(admin);
        petsController.refreshPets();
        usersController.refreshUsers();
        registrationsController.refreshRegistrations();
        incidentsController.refreshIncidents();
    }
}
