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
    public void openClientRegistration(ActionEvent event) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Client Registration");
        dialog.setHeaderText("Enter client details:");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("First name Last name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("+370...");
        TextField emailField = new TextField();
        emailField.setPromptText("your.name@example.com");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, registerButtonType, () -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }
            if (phone.length() < 12) {
                throw new IllegalArgumentException("Phone number cannot be shorter than 12 characters.");
            }
            if (phone.length() > 12) {
                throw new IllegalArgumentException("Phone number cannot be longer than 12 characters.");
            }
            if (!phone.startsWith("+")) {
                throw new IllegalArgumentException("Phone number must start with '+'.");
            }
            for (int i = 1; i < phone.length(); i++) {
                char character = phone.charAt(i);
                if (character < '0' || character > '9') {
                    throw new IllegalArgumentException("Phone number must contain only digits after '+'.");
                }
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Email address must contain '@'.");
            }
            return new Client(name, phone, email);
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            DuomBaze.getClients().add(client);
            showMessage("Success", "Client " + client.getName() + " registered successfully!");
        });
    }

    // VETERINARO REGISTRACIJA
    @FXML
    public void openVetRegistration(ActionEvent event) {
        Dialog<Vet> dialog = new Dialog<>();
        dialog.setTitle("Vet Registration");
        dialog.setHeaderText("Enter vet details:");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("First name Last name");
        TextField specializationField = new TextField();
        specializationField.setPromptText("e.g. surgeon");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Specialization:"), 0, 1);
        grid.add(specializationField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, registerButtonType, () -> {
            String name = nameField.getText();
            String specialization = specializationField.getText();

            if (name.isEmpty() || specialization.isEmpty()) {
                throw new IllegalArgumentException("Name and specialization must be filled in.");
            }
            return new Vet(name, specialization);
        });

        Optional<Vet> result = dialog.showAndWait();
        result.ifPresent(vet -> {
            DuomBaze.getVets().add(vet);
            showMessage("Success", "Vet " + vet.getName() + " registered successfully!");
        });
    }

    // AUGINTINIO REGISTRACIJA
    @FXML
    public void openPetRegistration(ActionEvent event) {
        Dialog<Pet> dialog = new Dialog<>();
        dialog.setTitle("Pet Registration");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        ComboBox <PetType> typeField = new ComboBox<>();
        typeField.getItems().setAll(PetType.values());
        typeField.setPromptText("Pet type");
        TextField ageField = new TextField();
        TextField weightField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Species:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Age (years):"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Weight (kg):"), 0, 3);
        grid.add(weightField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, saveButtonType, () -> {
            try {
                if (nameField.getText().isEmpty()
                        || ageField.getText().isEmpty()
                        || weightField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled in!");
                }

                int age = Integer.parseInt(ageField.getText());
                double weight = Double.parseDouble(weightField.getText());
                return new Pet(nameField.getText(), typeField.getValue(), age, weight);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Age and weight must be numbers!", e);
            }
        });

        Optional<Pet> result = dialog.showAndWait();
        result.ifPresent(pet -> {
            DuomBaze.getPets().add(pet);
            showMessage("Success", "Pet " + pet.getName() + " added successfully!");
        });
    }

    // AUGINTINIO PRISKYRIMAS KLIENTUI
    @FXML
    public void atidarytiPriskyrima(ActionEvent event) {
        if (DuomBaze.getClients().isEmpty() || DuomBaze.getPets().isEmpty()) {
            showError("Error",
                    "First add at least one client and one pet!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign Pet");

        ChoiceBox<Client> clientChoiceBox = new ChoiceBox<>();
        clientChoiceBox.getItems().addAll(DuomBaze.getClients());

        ChoiceBox<Pet> petChoiceBox = new ChoiceBox<>();
        petChoiceBox.getItems().addAll(DuomBaze.getPets());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Select client:"), 0, 0);
        grid.add(clientChoiceBox, 1, 0);
        grid.add(new Label("Select pet:"), 0, 1);
        grid.add(petChoiceBox, 1, 1);


        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dataValidation(dialog, ButtonType.OK, () -> {
            Client client = clientChoiceBox.getValue();
            Pet pet = petChoiceBox.getValue();

            if (client == null || pet == null) {
                throw new IllegalArgumentException("Select both a client and a pet.");
            }

            if (pet.getOwner() != null) {
                throw new IllegalArgumentException(
                        "This pet already has an assigned owner: "
                                + pet.getOwner().getName() + " ("
                                + pet.getOwner().getPhone() + ")."
                );
            }

            client.registerPet(pet);
            showMessage(
                    "Success",
                    "Pet " + pet.getName() + " assigned to owner " + pet.getOwner().getName() + "!"
            );
            return null;
        });

        dialog.showAndWait();
    }

    // INFORMACIJOS RODYMAS
    @FXML
    public void openInformation(ActionEvent event) {
        if (DuomBaze.getPets().isEmpty()) {
            showError("Error", "No pets found.");
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

        for (Client client : DuomBaze.getClients()) {
            sb.append("Client: ")
                    .append(client.getName())
                    .append(" (Phone: ")
                    .append(client.getPhone())
                    .append(", Email: ")
                    .append(client.getEmail())
                    .append(")\n");

            sb.append("Pets:\n");
            for (Pet pet : client.getPets()) {
                sb.append("  - ").append(pet.getInfo()).append("\n");
            }
            sb.append("------------------------------\n");
        }

        if (sb.length() == 0) {
            sb.append("No registered data found.");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pet Information");
        alert.setHeaderText("Client and pet list");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private <T> void dataValidation(Dialog<T> dialog, ButtonType submitButton,
            Supplier<T> validator) {
        ObjectProperty<T> validResult = new SimpleObjectProperty<>();
        dialog.getDialogPane().lookupButton(submitButton).addEventFilter(ActionEvent.ACTION, event -> {
            try {
                validResult.set(validator.get());
            } catch (IllegalArgumentException e) {
                // Keep the dialog open so the user can correct the entered data.
                event.consume();
                showError("Error", e.getMessage());
            }
        });
        dialog.setResultConverter(button -> button == submitButton ? validResult.get() : null);
        // jeigu submitButton - true(clicked), tada gerasusRezultatas.get() grazins rezultata, kitaip grazins null
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
