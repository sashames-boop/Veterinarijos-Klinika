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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static controller.DialogUtils.dataValidation;
import static controller.DialogUtils.showError;
import static controller.DialogUtils.showMessage;

public class RegistrationsController {
    private CustomHibernate custom;

    //-------Registracijoms-----------//
    @FXML
    private TableView<Registration> regTable;
    @FXML
    private TableColumn<Registration, Integer> regIdColumn;
    @FXML
    private TableColumn<Registration, String> dateColumn;
    @FXML
    private TableColumn<Registration, String> timeColumn;
    @FXML
    private TableColumn<Registration, String> clientColumn;
    @FXML
    private TableColumn<Registration, String> petColumn;
    @FXML
    private TableColumn<Registration, String> vetColumn;
    @FXML
    private TableColumn<Registration, String> groomerColumn;
    @FXML
    private TableColumn<Registration, String> statusColumn;
    @FXML
    private TextField searchField;
    private final ObservableList<Registration> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        custom = MainApp.custom;
        //Registraciju lentele
        regIdColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        dateColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getDate()));
        timeColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getTime()));
        clientColumn.setCellValueFactory(c -> {
            Client client = c.getValue().getClient();
            return new ReadOnlyObjectWrapper<>(client == null ? "-" : client.getName());
        });
        petColumn.setCellValueFactory(c -> {
            Pet pet = c.getValue().getPet();
            return new ReadOnlyObjectWrapper<>(pet == null ? "-" : pet.getName());
        });
        vetColumn.setCellValueFactory(c -> {
            Vet vet = c.getValue().getVet();
            return new ReadOnlyObjectWrapper<>(vet == null ? "-" : vet.getName());
        });
        groomerColumn.setCellValueFactory(c -> {
            Groomer groomer = c.getValue().getGroomer();
            return new ReadOnlyObjectWrapper<>(groomer == null ? "-" : groomer.getName());
        });
        statusColumn.setCellValueFactory(c -> {
            Registration registration = c.getValue();
            String status = "Pending";
            if (registration.isCancelled()) {
                status = "Cancelled";
            } else if (registration.isConfirmed()) {
                status = "Confirmed";
            }
            return new ReadOnlyObjectWrapper<>(status);
        });

        TableSearch.setup(regTable, searchField, data);

    }

    //REGISTRACIJU TAB-----------------------------------------------------------------------------------------------------------------------
    //PRIDETI REGISTRACIJA
    public void openRegistrationAdd(ActionEvent actionEvent) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add registration");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ChoiceBox<Pet> petChoice = new ChoiceBox<>();
        petChoice.getItems().setAll(custom.getAll(Pet.class, "owner"));

        ChoiceBox<User> specialistChoice = new ChoiceBox<>();
        specialistChoice.getItems().setAll(custom.getUsersBytType(List.of(Vet.class, Groomer.class)));

        DatePicker dateField = new DatePicker();
        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Pet:"), petChoice);
        grid.addRow(1, new Label("Specialist:"), specialistChoice);
        grid.addRow(2, new Label("Date:"), dateField);
        grid.addRow(3, new Label("Time:"), timeField);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, ButtonType.OK, () -> {
            Pet pet = petChoice.getValue();
            User specialist = specialistChoice.getValue();

            if (pet == null || specialist == null || dateField.getValue() == null) {
                throw new IllegalArgumentException("Please select a pet, specialist and date.");
            }

            LocalTime time;
            try {
                time = LocalTime.parse(timeField.getText().trim());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Enter a valid time!");
            }

            try {
                custom.createRegistration(
                        pet.getId(), specialist.getId(),
                        dateField.getValue().toString(), time.toString());
            } catch (PersistenceException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not save the registration.", e);
            }

            refreshRegistrations();
            return null;
        });

        dialog.showAndWait();
    }

    //REDAGUOTI REGISTRACIJA
    public void openRegistrationEdit(ActionEvent actionEvent) {
        Registration registration = regTable.getSelectionModel().getSelectedItem();
        if(registration == null){
            showMessage("Error", "Please select a registration!");
            return;
        }

        Dialog<Registration> dialog = new Dialog<>();
        dialog.setTitle("Edit registration");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        DatePicker dateField = new DatePicker(LocalDate.parse(registration.getDate()));
        TextField timeField = new TextField(registration.getTime());
        timeField.setPromptText("HH:mm");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        grid.addRow(0, new Label("Date:"), dateField);
        grid.addRow(1, new Label("Time:"), timeField);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, saveButtonType, () -> {
            if(dateField.getValue() == null){
                throw new IllegalArgumentException("Please enter a valid date!");
            }

            LocalTime time;
            try {
                time = LocalTime.parse(timeField.getText().trim());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Enter a valid time!");
            }

            registration.setDate(dateField.getValue().toString());
            registration.setTime(timeField.getText().trim());


            return registration;
        });


        dialog.showAndWait().ifPresent(result -> {
            try {
                custom.update(result);
                refreshRegistrations();

            } catch (PersistenceException e) {
                e.printStackTrace();
                showMessage("Error", "Could not save the registration.");
                return;
            }

            refreshRegistrations();

            showMessage("Success", "Registration edited successfully!");
        });

    }

    //ISTRINTI REGISTRACIJA
    @FXML
    public void openRegistrationDelete(ActionEvent event) {
        Registration registration = regTable.getSelectionModel().getSelectedItem();
        if (registration == null) {
            showError("Error", "Please select a registration!");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete registration (ID " + registration.getId() + ")?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirm.setTitle("Delete registration");
        confirm.setHeaderText("Are you sure?");

        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        try {
            custom.delete(registration);
        } catch (PersistenceException e) {
            e.printStackTrace();
            showError("Error", "Could not delete the registration.");
            return;
        }

        refreshRegistrations();
        showMessage("Success", "Registration deleted successfully!");
    }

    void refreshRegistrations() {
        data.setAll(
                custom.getAll(Registration.class, "client pet vet groomer")
        );
    }
}
