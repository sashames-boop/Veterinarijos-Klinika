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

public class LoginForm {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void validateLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Error", "Enter your login credentials.");
            return;
        }

        LoginStatus status = DuomBaze.validateLogin(username, password);

        if (status == LoginStatus.SUCCESS) {
//            showMessage("Success", "Logged in successfully!");

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/veterinarija/main-form.fxml"))
            );
            stage.setScene(scene);
            stage.show();
        } else if (status == LoginStatus.WRONG_PASSWORD) {
            showError("Error", "Incorrect password.");
        } else if (status == LoginStatus.USER_NOT_FOUND) {
            showError("Error", "User not found. Please register.");
            openRegistration();
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
