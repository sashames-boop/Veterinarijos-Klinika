package controller;

import javafx.event.ActionEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.function.Supplier;

import models.*;

public class MainForm {

    // KLIENTO REGISTRACIJA
    @FXML
    public void atidarytiKlientoReg(ActionEvent event) {
        Dialog<Klientas> dialog = new Dialog<>();
        dialog.setTitle("Kliento Registracija");
        dialog.setHeaderText("Įveskite kliento duomenis:");

        ButtonType registruotiButtonType = new ButtonType("Registruoti", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registruotiButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField vardasField = new TextField();
        vardasField.setPromptText("Vardas Pavardė");
        TextField telefonasField = new TextField();
        telefonasField.setPromptText("+370...");
        TextField elPastasField = new TextField();
        elPastasField.setPromptText("jusu.vardas@example.com");

        grid.add(new Label("Vardas:"), 0, 0);
        grid.add(vardasField, 1, 0);
        grid.add(new Label("Telefonas:"), 0, 1);
        grid.add(telefonasField, 1, 1);
        grid.add(new Label("El. paštas:"), 0, 2);
        grid.add(elPastasField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        duomenuPatikrinimas(dialog, registruotiButtonType, () -> {
            String vardas = vardasField.getText();
            String pastas = elPastasField.getText();
            String tel = telefonasField.getText();

            if (vardas.isEmpty()) {
                throw new IllegalArgumentException("Vardas negali būti tuščias.");
            }
            if (tel.length() < 12)
                throw new IllegalArgumentException("Telefono numeris negali būti trumpesnis už 12 simbolių.");
            if (tel.length() > 12)
                throw new IllegalArgumentException("Telefono numeris negali būti ilgesnis nei 12 simbolių.");
            if (!tel.startsWith("+"))
                throw new IllegalArgumentException("Telefono numeris privalo prasidėti iš '+' simbolio.");
            for (int i = 1; i < tel.length(); i++) {
                char simb = tel.charAt(i);
                if (simb < '0' || simb > '9')
                    throw new IllegalArgumentException("Telefono numeris privalo būti sudarytas iš skaičių.");
            }
            if (!pastas.contains("@")) throw new IllegalArgumentException("Paštas turi būti užrašytas su @ simboliu.");
            return new Klientas(vardas, tel, pastas);
        });

        Optional<Klientas> result = dialog.showAndWait();
        result.ifPresent(klientas -> {
            DuomBaze.getKlientai().add(klientas);
            rodytiPranesima("Sėkmė", "Klientas " + klientas.getVardas() + " sėkmingai užregistruotas!");
        });
    }

    // VETERINARO REGISTRACIJA
    public void atidarytiVeterinaroReg(ActionEvent event) {
        Dialog<Veterinaras> dialog = new Dialog<>();
        dialog.setTitle("Veterinaro Registracija");
        dialog.setHeaderText("Įveskite veterinaro duomenis:");

        ButtonType registruotiButtonType = new ButtonType("Registruoti", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registruotiButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField vardasField = new TextField();
        vardasField.setPromptText("Vardas Pavardė");
        TextField specField = new TextField();
        specField.setPromptText("Pvz. chirurgas");

        grid.add(new Label("Vardas:"), 0, 0);
        grid.add(vardasField, 1, 0);
        grid.add(new Label("Specialybė:"), 0, 1);
        grid.add(specField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        duomenuPatikrinimas(dialog, registruotiButtonType, () -> {
            String vardas = vardasField.getText();
            String specializacija = specField.getText();

            if (vardas.isEmpty() || specializacija.isEmpty()) {
                throw new IllegalArgumentException("Vardas ir/ar specializacija turi būti užpildyti.");
            }
            return new Veterinaras(vardas, specializacija);
        });

        Optional<Veterinaras> result = dialog.showAndWait();
        result.ifPresent(veterinaras -> {
            DuomBaze.getVeterinarai().add(veterinaras);
            rodytiPranesima("Sekmė", "Veterinaras " +
                    veterinaras.getVardas() + " sėkmingai užregistruotas!");
        });
    }

    // AUGINTINIO REGISTRACIJA
    @FXML
    public void atidarytiAugintinioReg(ActionEvent event) {
        Dialog<Augintinis> dialog = new Dialog<>();
        dialog.setTitle("Augintinio Registracija");

        ButtonType registruotiButtonType = new ButtonType("Išsaugoti", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registruotiButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField vardasField = new TextField();
        TextField rusisField = new TextField();
        TextField amziusField = new TextField();
        TextField svorisField = new TextField();

        grid.add(new Label("Vardas:"), 0, 0);
        grid.add(vardasField, 1, 0);
        grid.add(new Label("Rūšis:"), 0, 1);
        grid.add(rusisField, 1, 1);
        grid.add(new Label("Amžius(metai):"), 0, 2);
        grid.add(amziusField, 1, 2);
        grid.add(new Label("Svoris(kg):"), 0, 3);
        grid.add(svorisField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        duomenuPatikrinimas(dialog, registruotiButtonType, () -> {
            try {
                if (vardasField.getText().isEmpty() || rusisField.getText().isEmpty() || amziusField.getText().isEmpty() || svorisField.getText().isEmpty()) {
                    throw new IllegalArgumentException("Visi laukai turi būti užpildyti!");
                }
                int amzius = Integer.parseInt(amziusField.getText());
                double svoris = Double.parseDouble(svorisField.getText());
                return new Augintinis(vardasField.getText(), rusisField.getText(), amzius, svoris);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Amžius bei svoris turi būti skaičius!", e);
            }
        });

        Optional<Augintinis> result = dialog.showAndWait();
        result.ifPresent(augintinis -> {
            DuomBaze.getAugintiniai().add(augintinis);
            rodytiPranesima("Sėkmė", "Augintinis " + augintinis.getVardas() + " pridėtas!");
        });
    }

    // AUGINTINIO PRISKYRIMAS KLIENTUI
    @FXML
    public void atidarytiPriskyrima(ActionEvent event) {
        if (DuomBaze.getKlientai().isEmpty() || DuomBaze.getAugintiniai().isEmpty()) {
            rodytiKlaidosPranesima("Klaida", "Pirmiausia turite įvesti bent vieną klientą ir augintinį!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Priskirti augintinį");

        ChoiceBox<Klientas> klientoBox = new ChoiceBox<>();
        klientoBox.getItems().addAll(DuomBaze.getKlientai());

        ChoiceBox<Augintinis> augintinioBox = new ChoiceBox<>();
        augintinioBox.getItems().addAll(DuomBaze.getAugintiniai());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Pasirinkite Klientą:"), 0, 0);
        grid.add(klientoBox, 1, 0);
        grid.add(new Label("Pasirinkite Augintinį:"), 0, 1);
        grid.add(augintinioBox, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        duomenuPatikrinimas(dialog, ButtonType.OK, () -> {
            Klientas k = klientoBox.getValue();
            Augintinis a = augintinioBox.getValue();
            if (k == null || a == null) {
                throw new IllegalArgumentException("Pasirinkite klientą ir augintinį.");
            }

            if (a.getKlientas() != null) {
                throw new IllegalArgumentException("Šis augintinis jau turi jam priskirtą šeimininką: " +
                        a.getKlientas().getVardas() + " (" + a.getKlientas().getTel() + ").");
            }
            k.uzregistruotiAugintini(a);
            rodytiPranesima("Sėkmė", "Augintinis " + a.getVardas() + " priskirtas prie " +
                    a.getKlientas().getVardas() + " šeimininko!");
            return null;
        });
        dialog.showAndWait();
    }

    // INFORMACIJOS RODYMAS
    @FXML
    public void atidarytiInformacija(ActionEvent event) {
        if (DuomBaze.getAugintiniai().isEmpty()) {
            rodytiKlaidosPranesima("Klaida", "Nieko nerasta augintinių sąraše :(");
            return;
        }
//        Dialog<Void> dialog = new Dialog<>();
//        dialog.setTitle("Informacija");
//
//        ChoiceBox<Klientas> klientoBox = new ChoiceBox<>();
//        klientoBox.getItems().addAll(DuomBaze.getKlientai());
//        ChoiceBox<Augintinis> augintinioBox = new ChoiceBox<>();
//        augintinioBox.getItems().addAll(DuomBaze.getAugintiniai());
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.add(new Label("Klientas:"), 0, 0);
//        grid.add(klientoBox, 1, 0);
//        grid.add(new Label("Augintinis:"), 0, 1);
//        grid.add(augintinioBox, 1, 1);
//
//        dialog.getDialogPane().setContent(grid);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        duomenuPatikrinimas(dialog, );

        StringBuilder sb = new StringBuilder();
        for (Klientas k : DuomBaze.getKlientai()) {
            sb.append("Klientas: ").append(k.getVardas()).append(" (Tel: ").append(k.getTel()).
                    append("El. paštas: ").append(k.getElPastas()).append(")\n");
            sb.append("Augintiniai:\n");
            for (Augintinis a : k.getAugintiniai()) {
                sb.append("  - ").append(a.gautiInfo()).append("\n");
            }
            sb.append("------------------------------\n");
        }

        if (sb.length() == 0) {
            sb.append("Nėra jokių užregistruotų duomenų.");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Augintinių Informacija");
        alert.setHeaderText("Klientų ir augintinių sąrašas");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private <T> void duomenuPatikrinimas(Dialog<T> dialog, ButtonType submitButton, Supplier<T> tikrinti) {
        ObjectProperty<T> gerasRezultatas = new SimpleObjectProperty<>();
        dialog.getDialogPane().lookupButton(submitButton).addEventFilter(ActionEvent.ACTION, event -> {
            try {
                gerasRezultatas.set(tikrinti.get());
            } catch (IllegalArgumentException e) {
                event.consume(); // Sustabdyti dialoga, kad info liktu langelyje
                rodytiKlaidosPranesima("Klaida", e.getMessage());
            }
        });
        dialog.setResultConverter(button -> button == submitButton ? gerasRezultatas.get() : null);
        // jeigu submitButton - true(clicked), tada gerasusRezultatas.get() grazins rezultata, kitaip grazins null
    }

    private void rodytiPranesima(String antraste, String tekstas) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(antraste);
        alert.setHeaderText(null);
        alert.setContentText(tekstas);
        alert.showAndWait();
    }

    private void rodytiKlaidosPranesima(String antraste, String tekstas) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(antraste);
        alert.setHeaderText(null);
        alert.setContentText(tekstas);
        alert.showAndWait();
    }
}
