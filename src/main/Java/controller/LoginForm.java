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
    private TextField usrnameField;
    @FXML
    private PasswordField pswField;

    @FXML
    public void patikrintiLogin() throws IOException {
        String username = usrnameField.getText();
        String password = pswField.getText();

        LoginStatus status = DuomBaze.patikrintiLogin(username, password);
        if (username.isEmpty() || password.isEmpty()) {
            rodytiKlaidosPranesima("Klaida", "Įveskite prisijungimo duomenis.");
        }
        if (status == LoginStatus.SUCCESS) {
            rodytiPranesima("Sekmė", "Sekmingai prisijungta prie sistemos!");
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/veterinarija/main-form.fxml")));
        }
        else if (status == LoginStatus.WRONG_PSW){
            rodytiKlaidosPranesima("Klaida", "Neteisingas slaptažodis.");
        }
        else if (status == LoginStatus.USER_NOT_FOUND){
            rodytiKlaidosPranesima("Klaida", "Nėra tokio vartotojo. Kviečiame Jus užsiregistruoti!");
        }
    }
    public void atidarytiReg() throws IOException {

    }

    private void rodytiKlaidosPranesima(String antraste, String tekstas) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(antraste);
        alert.setHeaderText(null);
        alert.setContentText(tekstas);
        alert.showAndWait();
    }

    private void rodytiPranesima(String antraste, String tekstas) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(antraste);
        alert.setHeaderText(null);
        alert.setContentText(tekstas);
        alert.showAndWait();
    }
}
