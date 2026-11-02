package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RegForm {
    @FXML
    private Button backButton;
    @FXML
    public void goBackLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/veterinarija/login-form.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
