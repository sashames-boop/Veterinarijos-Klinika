package controller;

import hibernate.CustomHibernate;
import jakarta.persistence.PersistenceException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import main.MainApp;
import models.*;
import java.util.List;
import java.util.Optional;

import static controller.DialogUtils.dataValidation;
import static controller.DialogUtils.showError;
import static controller.DialogUtils.showMessage;

public class PetsController {
    private CustomHibernate custom;

    //-----Augintiniams--------//
    @FXML
    private TableView<Pet> petsTable;
    @FXML
    private TableColumn<Pet, Integer> petIdColumn;
    @FXML
    private TableColumn<Pet, String> petNameColumn;
    @FXML
    private TableColumn<Pet, Integer> petAgeColumn;
    @FXML
    private TableColumn<Pet, Double> petWeightColumn;
    @FXML
    private TableColumn<Pet, PetType> petTypeColumn;
    @FXML
    private TableColumn<Pet, String> petOwnerColumn;
    @FXML
    private TextField searchField;
    private final ObservableList<Pet> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        custom = MainApp.custom;
        //PET lenteles stulpeliai
        petIdColumn.setCellValueFactory( c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        petNameColumn.setCellValueFactory( c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getName()));
        petAgeColumn.setCellValueFactory( c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getAge()));
        petWeightColumn.setCellValueFactory( c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getWeight()));
        petTypeColumn.setCellValueFactory( c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getType()));
        petOwnerColumn.setCellValueFactory( c ->
                {
                    Client owner = c.getValue().getOwner();
                    return new ReadOnlyObjectWrapper<>(
                            owner == null ? "Unassigned" : owner.getName()
                        );
                });

        TableSearch.setup(petsTable, searchField, data);

    }

    //VEIKSMAI SU AUGINTINIAIS--------------------------------------------------------------------------------------------------------------------------------
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
                        || typeField.getValue() == null
                        || ageField.getText().isEmpty()
                        || weightField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled in!");
                }

                int age = Integer.parseInt(ageField.getText());
                double weight = Double.parseDouble(weightField.getText());
                if (age < 0 || !Double.isFinite(weight) || weight <= 0) {
                    throw new IllegalArgumentException("Age cannot be negative and weight must be a positive finite number.");
                }
                return new Pet(nameField.getText(), typeField.getValue(), age, weight);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Age and weight must be numbers!", e);
            }
        });

        Optional<Pet> result = dialog.showAndWait();
        result.ifPresent(pet -> {
            custom.create(pet);
            refreshPets();
            showMessage("Success", "Pet " + pet.getName() + " added successfully!");
        });
    }

    //AUGINTINIO REDAGAVIMAS
    @FXML
    public void openPetEdit(ActionEvent event) {
        Pet pet = petsTable.getSelectionModel().getSelectedItem();
        if(pet == null){
            showMessage("Error", "Please select a pet!");
            return;
        }
        Dialog<Pet> dialog = new Dialog<>();
        dialog.setTitle("Edit Pet");

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

        nameField.setText(pet.getName());
        typeField.setValue(pet.getType());
        ageField.setText(String.valueOf(pet.getAge()));
        weightField.setText(String.valueOf(pet.getWeight()));


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
                        || typeField.getValue() == null
                        || ageField.getText().isEmpty()
                        || weightField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled in!");
                }

                int age = Integer.parseInt(ageField.getText());
                double weight = Double.parseDouble(weightField.getText());
                if (age < 0 || !Double.isFinite(weight) || weight <= 0) {
                    throw new IllegalArgumentException("Age cannot be negative and weight must be a positive finite number.");
                }
                pet.setName(nameField.getText());
                pet.setType(typeField.getValue());
                pet.setAge(age);
                pet.setWeight(weight);
                return pet;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Age and weight must be numbers!", e);
            }
        });


        dialog.showAndWait().ifPresent(result -> {
            custom.update(result);
            refreshPets();
            showMessage("Success", "Pet " + pet.getName() + " edited successfully!");
        });
    }

    //ISTRINTI AUGINTI
    @FXML
    public void openPetDelete(ActionEvent event) {
        Pet pet = petsTable.getSelectionModel().getSelectedItem();
        if(pet == null){
            showMessage("Error", "Please select a pet!");
        }

        Dialog<Pet> dialog = new Dialog<>();
        dialog.setTitle("Delete Pet");

        Alert confirm = new  Alert(Alert.AlertType.CONFIRMATION,
        "Delete " + pet.getName() + "(ID " + pet.getId() + ")?",
                ButtonType.YES,
                ButtonType.NO);
        confirm.setTitle("Delete Pet");
        confirm.setHeaderText("Are you sure?");

        if(confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        try {
            custom.delete(pet);
        } catch (PersistenceException e) {
            e.printStackTrace();
            showMessage("Error", "Could not delete pet!");
            return;
        }
        refreshPets();
        showMessage("Success", "Pet " + pet.getName() + " deleted.");
    }

    // AUGINTINIO PRISKYRIMAS KLIENTUI
    @FXML
    public void openAssigning(ActionEvent event) {
        List<Client> clients = custom.getAll(Client.class, "");
        List<Pet> pets = custom.getAll(Pet.class,"");


        if (clients.isEmpty() || pets.isEmpty()) {
            showError("Error",
                    "First add at least one client and one pet!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign Pet");

        ChoiceBox<Client> clientChoiceBox = new ChoiceBox<>();
        clientChoiceBox.getItems().setAll(clients);

        ChoiceBox<Pet> petChoiceBox = new ChoiceBox<>();
        petChoiceBox.getItems().setAll(pets);

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

            custom.assignPet(client.getId(), pet.getId());
            refreshPets();
            showMessage(
                    "Success",
                    "Pet " + pet.getName() + " assigned to owner " + client.getName() + "!"
            );
            return null;
        });

        dialog.showAndWait();
    }

    // INFORMACIJOS RODYMAS--------------------------------------------------------------------------------------------------------------------------------
    @FXML
    public void openInformation(ActionEvent event) {
        List<Client> clients = custom.getAll(Client.class, "pets");
        StringBuilder sb = new StringBuilder();

        for (Client client : clients) {
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

    void refreshPets() {

        data.setAll(custom.getAll(Pet.class, "owner"));
    }
}
