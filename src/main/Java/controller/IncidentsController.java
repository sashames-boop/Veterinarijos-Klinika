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
import java.time.LocalDateTime;

import static controller.DialogUtils.dataValidation;
import static controller.DialogUtils.showError;
import static controller.DialogUtils.showMessage;

public class IncidentsController {
    private CustomHibernate custom;

    //------Incidentams---------//
    @FXML
    private TableView<Incident> incidentsTable;
    @FXML
    private TableColumn<Incident, Integer> incidentIdColumn;
    @FXML
    private TableColumn<Incident, String> incidentCommentColumn;
    @FXML
    private TableColumn<Incident, IncidentStatus> incidentStatusColumn;
    @FXML
    private TableColumn<Incident, LocalDateTime> incidentCreatedColumn;
    @FXML
    private TableColumn<Incident, LocalDateTime> incidentModifiedColumn;
    @FXML
    private TextField searchField;
    private final ObservableList<Incident> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        custom = MainApp.custom;
        //Incidentu lentele
        incidentIdColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        incidentCommentColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getComment()));
        incidentStatusColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getStatus()));
        incidentCreatedColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getRegistrationDate()));
        incidentModifiedColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getModificationDate()));

        TableSearch.setup(incidentsTable, searchField, data);

    }

    //INCIDENTU VEIKSMAI---------------------------------------------------------------------------------------------
    //INCIDENTO SUKURIMAS
    public void openIncidentAdd(ActionEvent actionEvent) {
        Incident incident = new Incident();
        Dialog<Incident> dialog = new Dialog<>();
        dialog.setTitle("Add incident");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextArea commentField = new TextArea();


        ChoiceBox<IncidentStatus> incidentStatus= new ChoiceBox<>();
        incidentStatus.getItems().setAll(IncidentStatus.values());
        incidentStatus.setValue(IncidentStatus.CREATED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Comment:"), commentField);
        grid.addRow(1, new Label("Status:"), incidentStatus);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, saveButton, () -> {
            String comment = commentField.getText().trim();
            IncidentStatus status = incidentStatus.getValue();

            if(comment.isEmpty() || comment.isBlank()){
                throw new IllegalArgumentException("Please enter a valid comment!");
            }
            if(status == null) {
                throw new IllegalArgumentException("Please enter a valid status!");
            }

            LocalDateTime now = LocalDateTime.now().withNano(0);
            return new Incident(now, now, status, comment);


        });
        dialog.showAndWait().ifPresent(result -> {
            try {
                custom.create(result);
            } catch (PersistenceException e) {
                e.printStackTrace();
                showError("Error", "Could not create the incident.");
                return;
            }
            refreshIncidents();
        });
    }

    //REDAGUOTI INCIDENTA
    @FXML
    public void openIncidentEdit(ActionEvent actionEvent) {
            Incident incident = incidentsTable.getSelectionModel().getSelectedItem();

            if (incident == null) {
                showError("Error", "Please select an incident!");
                return;
            }

            Dialog<Incident> dialog = new Dialog<>();
            dialog.setTitle("Edit incident");

            ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            TextArea commentField = new TextArea();
            commentField.setText(incident.getComment());

            ChoiceBox<IncidentStatus> incidentStatus = new ChoiceBox<>();
            incidentStatus.getItems().setAll(IncidentStatus.values());
            incidentStatus.setValue(incident.getStatus());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            grid.addRow(0, new Label("Comment:"), commentField);
            grid.addRow(1, new Label("Status:"), incidentStatus);

            dialog.getDialogPane().setContent(grid);

            dataValidation(dialog, saveButton, () -> {

                String comment = commentField.getText().trim();
                IncidentStatus status = incidentStatus.getValue();

                if (comment.isEmpty() || comment.isBlank()) {
                    throw new IllegalArgumentException(
                            "Please enter a valid comment!"
                    );
                }

                if (status == null) {
                    throw new IllegalArgumentException(
                            "Please select a valid status!"
                    );
                }

                incident.setComment(comment);
                incident.setStatus(status);

                incident.setModificationDate(
                        LocalDateTime.now().withNano(0)
                );

                return incident;
            });

            dialog.showAndWait().ifPresent(result -> {
                try {
                    custom.update(result);
                } catch (PersistenceException e) {
                    e.printStackTrace();
                    showError("Error", "Could not update the incident.");
                    return;
                }

                refreshIncidents();

                showMessage("Success", "Incident edited successfully!");
            });
    }

    //ISTRINTI INCIDENTA
    @FXML
    public void openIncidentDelete(ActionEvent actionEvent) {
            Incident incident =
                    incidentsTable.getSelectionModel().getSelectedItem();

            if (incident == null) {
                showError("Error", "Please select an incident!");
                return;
            }

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Delete incident (ID " + incident.getId() + ")?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            confirm.setTitle("Delete incident");
            confirm.setHeaderText("Are you sure?");

            if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) { return ;}

            try {
                custom.delete(incident);
            } catch (PersistenceException e) {
                e.printStackTrace();
                showError(
                        "Error",
                        "Could not delete the incident."
                );
                return;
            }

            refreshIncidents();

            showMessage("Success", "Incident deleted successfully!");
    }

    void refreshIncidents() {

        data.setAll(custom.getAll(Incident.class, ""));
    }
}
