package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import main.MainApp;
import jakarta.persistence.PersistenceException;

public class LoginForm {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void validateLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        LoginStatus status;
        User user = null;

        if (username.isEmpty() || password.isEmpty()) {
            showError("Error", "Enter your login credentials.");
            return;
        }

        try {
            status = MainApp.custom.validateLogin(username, password);
            if (status == LoginStatus.SUCCESS) {
                user = MainApp.custom.getUserByUsername(username);
            } else if (status == LoginStatus.BLOCKED) {
                showError("Access denied", "This account is blocked.");
            }
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            showError("Error", "Invalid username or password. Please try again");
            return;
        }

        if (status == LoginStatus.SUCCESS) {
//            showMessage("Success", "Logged in successfully!");

            if(!(user instanceof Admin admin)) {
                showError("Error", "Access denied.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/veterinarija/main-form.fxml"));
            Scene scene =  new Scene(loader.load());

            MainForm mainForm = (MainForm) loader.getController();
            mainForm.setAdmin(admin);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(" Veterinary Clinic (Admin) ");
            stage.show();
        } else if (status == LoginStatus.WRONG_PASSWORD) {
            showError("Error", "Incorrect password.");
        } else if (status == LoginStatus.USER_NOT_FOUND) {
            showError("Error", "User not found. Please check your username and password.");
        }
    }
    @FXML
    public void openRegistration() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/veterinarija/reg-form.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) usernameField.getScene().getWindow();

        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
