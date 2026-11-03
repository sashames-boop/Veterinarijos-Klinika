package controller;

import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.MainApp;
import models.Client;
import models.Groomer;
import models.User;
import models.Vet;

import java.io.IOException;

public class RegForm {
    @FXML
    private Button backButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField specializationField;
    @FXML
    private Label emailLabel;
    @FXML
    private Label specializationLabel;
    @FXML
    private ChoiceBox<String> userTypeChoice;
    @FXML
    private Button registerButton;

    @FXML
    public void initialize(){
        userTypeChoice.getItems().addAll("Client", "Vet", "Groomer");
        userTypeChoice.setValue("Client");

        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateRegForm();
                });
        updateRegForm();
    }

    @FXML
    public void updateRegForm() {
        boolean isClient = userTypeChoice.getValue().equals("Client");
        boolean isVet = userTypeChoice.getValue().equals("Vet");
        boolean isGroomer = userTypeChoice.getValue().equals("Groomer");

        emailLabel.setVisible(isClient);
        emailField.setVisible(isClient);

        specializationLabel.setVisible(isVet || isGroomer);
        specializationField.setVisible(isVet || isGroomer);
    }

    @FXML
    public void registerUser() {
        try {
            String type =  userTypeChoice.getValue();
            String name = nameField.getText().trim();
            String phone = phoneField.getText();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();


            //PATIKRINIMAI
            if (name.isBlank() ||  phone.isBlank() || username.isBlank() || password.isBlank()) {
                throw new IllegalArgumentException(
                        "Name, phone, username and password cannot be blank!"
                );
            }
            //vardas
            if(name.length() < 3 || name.length() > 50){
                throw new IllegalArgumentException(
                        "Name must be between 3 and 50 characters!"
                );
            }
            //slaptazodis 2FA
            if (!password.equals(confirmPasswordField.getText())) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            //pastas
            if (type.equals("Client") && emailField.getText().isBlank()) {
                throw new IllegalArgumentException(
                        "Email address cannot be blank!"
                );
            }
            //pastas
            if(type.equals("Client") && !emailField.getText().contains("@") ) {
                throw new IllegalArgumentException(
                        "Email is not valid!"
                );
            }
            //specializacija
            if((type.equals("Groomer") || type.equals("Vet")) && specializationField.getText().isBlank()) {
                throw new IllegalArgumentException(
                        "Specialization cannot be blank!"
                );
            }
            //telefonas
            if(!phone.matches("\\+[0-9]{11}")) {
                throw new IllegalArgumentException("Phone number is not valid. It should start with +..., and contain 11 digits.");
            }
            //username
            if (username.isBlank() || username.length() < 3 || username.length() > 20) {
                throw new IllegalArgumentException("Username should be between 3 and 20 characters.");
            }
            //slaptazodis
            if(password.isBlank() || password.length() < 6) {
                throw new IllegalArgumentException("Password should be at least 6 symbols long.");
            }

            User user;

            if("Client".equals(type)) {
                Client client = new Client();
                client.setEmail(emailField.getText());
                user = client;
            } else if("Vet".equals(type)) {
                Vet vet = new Vet();
                vet.setSpecialization(specializationField.getText());
                user = vet;
            }
            else if("Groomer".equals(type)) {
                Groomer groomer = new Groomer();
                groomer.setSpecialization(
                        specializationField.getText()
                );
                user = groomer;
            } else {
                throw new IllegalArgumentException("Invalid type");
            }

            if (MainApp.custom.getUserByUsername(username) != null) {
                throw new IllegalArgumentException("Username already exists");
            }


            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setName(name);

            MainApp.custom.create(user);
        } catch (IllegalArgumentException e) {
            showError("Error", e.getMessage());
            return;
        } catch (PersistenceException e) {
            e.printStackTrace();
            showError("Error", "Could not create user. Check database connection.");
            return;
        }
        passwordField.clear();
        confirmPasswordField.clear();

        showMessage("Successfully registered user", "Successfully registered user");
    }

    @FXML
    public void goBackLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/veterinarija/login-form.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();



        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
