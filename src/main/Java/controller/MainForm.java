package controller;

import com.sun.tools.javac.Main;
import hibernate.CustomHibernate;
import jakarta.persistence.PersistenceException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javafx.scene.control.TextArea;
import java.time.LocalDateTime;

import models.*;
import main.MainApp;

public class MainForm {
    private CustomHibernate custom;
    private Admin admin;

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
    //--------User---------//
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> userPhoneColumn;
    @FXML
    private TableColumn<User, String> userRoleColumn;
    @FXML
    private ChoiceBox<String> userTypeFilter;
    @FXML
    private TableColumn<User, String> userIsBlockedColumn;
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

        //USER lenteles sulpteliai
        userIdColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        userNameColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getName()));
        usernameColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getUsername()));
        userPhoneColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getPhone()));
        userRoleColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getClass().getSimpleName()));

        userTypeFilter.getItems().setAll("All users", "Clients", "Staff", "Veterinarians", "Groomers", "Admins");
        userTypeFilter.setValue("All users");
        userTypeFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshUsers();
        });

        userIsBlockedColumn.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().isBlocked() ? "Blocked" : "Active"));

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
    }



    //VEIKSMAI SU NAUDOTOJAIS-------------------------------------------------------------------------------------------------------------------------------
    //BLOCKED mygtukas
    @FXML
    public void toggleUserBlocked(ActionEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            showError("Error", "Please select a user.");
            return;
        }

        if (user.getId().equals(admin.getId())) {
            showError("Error", "You cannot block your own account.");
            return;
        }

        boolean wasBlocked = user.isBlocked();
        user.setBlocked(!wasBlocked);

        try {
            custom.update(user);
        } catch (PersistenceException e) {
            user.setBlocked(wasBlocked);
            e.printStackTrace();
            showError("Error", "Could not change the account status.");
            return;
        }

        refreshUsers();
        showMessage("Success", wasBlocked ? "User unblocked." : "User blocked.");
    }


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
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new Label("Confirm password:"), 0, 5);
        grid.add(confirmPasswordField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, registerButtonType, () -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

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
            if (username.length() < 3 || username.length() > 20) {
                throw new IllegalArgumentException("Username should be between 3 and 20 characters.");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password should be at least 6 symbols long.");
            }
            if (!password.equals(confirmPasswordField.getText().trim())) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            try {
                if (CustomHibernate.getUserByUsername(username) != null) {
                    throw new IllegalArgumentException("Username already exists");
                }
            } catch (PersistenceException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not check the username. Check database connection.", e);
            }

            return new Client(name, phone, email, username, password);
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            try {
                custom.create(client);
            } catch (PersistenceException e) {
                e.printStackTrace();
                showError("Error", "Could not create user. Check database connection.");
                return;
            }
            refreshUsers();
            showMessage("Success", "Client " + client.getName() + " registered successfully!");
        });
    }

    // VETERINARO REGISTRACIJA
    @FXML
    public void openVetRegistration(ActionEvent event) {
        openSpecialistRegistration(Vet.class, Vet::new);
    }
    // GROOMERIO REGISTRACIJA
    @FXML
    public void openGroomerRegistration(ActionEvent event) {
        openSpecialistRegistration(Groomer.class, Groomer::new);
    }
    @FunctionalInterface
    private interface SpecialistFactory<T extends User> {
        T create(String name, String phone, String username, String password, String specialization);
    }
    private <T extends User> void openSpecialistRegistration(Class<T> roleType, SpecialistFactory<T> factory) {
        String role = roleType.getSimpleName();
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(role + " Registration");
        dialog.setHeaderText("Enter specialist details:");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("First name Last name");
        TextField specializationField = new TextField();
        specializationField.setPromptText("Specialization");
        TextField phoneField = new TextField();
        phoneField.setPromptText("+370...");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Specialization:"), 0, 1);
        grid.add(specializationField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new Label("Confirm password:"), 0, 5);
        grid.add(confirmPasswordField, 1, 5);
        dialog.getDialogPane().setContent(grid);

        dataValidation(dialog, registerButtonType, () -> {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String phone = phoneField.getText();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (name.length() < 3 || name.length() > 50) {
                throw new IllegalArgumentException("Name must be between 3 and 50 characters!");
            }
            if (specialization.isBlank()) {
                throw new IllegalArgumentException("Specialization cannot be blank!");
            }
            if (!phone.matches("\\+[0-9]{11}")) {
                throw new IllegalArgumentException("Phone number must start with + and contain 11 digits.");
            }
            if (username.length() < 3 || username.length() > 20) {
                throw new IllegalArgumentException("Username should be between 3 and 20 characters.");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password should be at least 6 symbols long.");
            }
            if (!password.equals(confirmPasswordField.getText().trim())) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            try {
                if (CustomHibernate.getUserByUsername(username) != null) {
                    throw new IllegalArgumentException("Username already exists");
                }
            } catch (PersistenceException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not check the username. Check database connection.", e);
            }

            return factory.create(name, phone, username, password, specialization);
        });

        Optional<T> result = dialog.showAndWait();
        result.ifPresent(user -> {
            try {
                custom.create(user);
            } catch (PersistenceException e) {
                e.printStackTrace();
                showError("Error", "Could not create user. Check database connection.");
                return;
            }
            refreshUsers();
            showMessage("Success", role + " " + user.getName() + " registered successfully!");
        });
    }

    //NAUDOTOJO REDAGAVIMAS
    @FXML
    public void openUserEdit(ActionEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null){
            showMessage("Error", "Please select a user!");
            return;
        }
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField usernameField = new TextField();
        TextField emailOrSpecField = new TextField();

        nameField.setText(user.getName());
        phoneField.setText(user.getPhone());
        usernameField.setText(user.getUsername());


        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);

        if (user instanceof Client client) {
            emailOrSpecField.setText(client.getEmail());
            grid.add(new Label("Email:"), 0, 3);
            grid.add(emailOrSpecField, 1, 3);
        } else if(user instanceof Vet vet) {
            emailOrSpecField.setText(vet.getSpecialization());
            grid.add(new Label("Email:"), 0, 3);
            grid.add(emailOrSpecField, 1, 3);
        } else if (user instanceof Groomer groomer) {
            emailOrSpecField.setText(groomer.getSpecialization());
            grid.add(new Label("Email:"), 0, 3);
            grid.add(emailOrSpecField, 1, 3);
        }
        dialog.getDialogPane().setContent(grid);


        dataValidation(dialog, saveButtonType, () -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();
            String emailOrSpec = emailOrSpecField.getText().trim();

                if (nameField.getText().isEmpty()
                        || phoneField.getText() == null
                        || usernameField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled in!");
                }

                if (name.length() < 3 || name.length() > 50) {
                    throw new IllegalArgumentException("Name must be between 3 and 50 characters.");
                }
                if (!phone.matches("\\+[0-9]{11}")) {
                    throw new IllegalArgumentException("Phone must start with + and contain 11 digits.");
                }
                if (username.length() < 3 || username.length() > 20) {
                    throw new IllegalArgumentException("Username must be between 3 and 20 characters.");
                }
                if (user instanceof Client && !emailOrSpec.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
                    throw new IllegalArgumentException("Enter a valid email address.");
                }
                if ((user instanceof Vet || user instanceof Groomer) && emailOrSpec.isBlank()) {
                    throw new IllegalArgumentException("Specialization cannot be blank.");
                }
                User existingUser;
                try {
                    existingUser = CustomHibernate.getUserByUsername(username);
                }catch (PersistenceException e) {
                    throw new IllegalArgumentException("Could not check the username. Check database connection.");
                }

                if(existingUser != null && !existingUser.getId().equals(user.getId())){
                    throw new IllegalArgumentException("Username already exists");
                }

                user.setName(name);
                user.setPhone(phone);
                user.setUsername(username);

                if(user instanceof Client client && !emailOrSpec.isBlank()) {
                    client.setEmail(emailOrSpec);
                } else if(user instanceof Vet vet && !emailOrSpec.isBlank()) {
                    vet.setSpecialization(emailOrSpec);
                } else if(user instanceof Groomer groomer && !emailOrSpec.isBlank()) {
                    groomer.setSpecialization(emailOrSpec);
                }

                return user;
        });


        dialog.showAndWait().ifPresent(result -> {
            try {
                custom.update(result);

            } catch (PersistenceException e) {
                e.printStackTrace();
                showError("Error", "Could not update user.");
                return;
            }
            refreshUsers();
            showMessage("Success", "Pet " + user.getName() + " edited successfully!");
        });
    }

    //ISTRINTI NAUDOTOJA
    @FXML
    public void openUserDelete(ActionEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null){
            showMessage("Error", "Please select a user to delete.");
            return;
        }
        if (user.getId().equals(admin.getId())) {
            showError("Error", "You cannot delete your own account.");
            return;
        }

        Dialog<Pet> dialog = new Dialog<>();
        dialog.setTitle("Delete User");

        Alert confirm = new  Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + user.getName() + "(ID " + user.getId() + ")?",
                ButtonType.YES,
                ButtonType.NO);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Are you sure?");

        if(confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        try {
            custom.delete(user);
        } catch (PersistenceException e) {
            e.printStackTrace();
            showMessage("Error", "Could not delete this user.");
            return;
        }
        refreshUsers();
        showMessage("Success", "User " + user.getName() + " deleted.");
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


    //PAPILDOMOS FUNKCIJOS---------------------------------------------------------------------------------------------------------------------------------
    private <T> void dataValidation(Dialog<T> dialog, ButtonType submitButton,
                                    Supplier<T> validator) {
        ObjectProperty<T> validResult = new SimpleObjectProperty<>();
        dialog.getDialogPane().lookupButton(submitButton).addEventFilter(ActionEvent.ACTION, event -> {
            try {
                validResult.set(validator.get());
            } catch (IllegalArgumentException e) {
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

    public void setAdmin(Admin admin) {
        this.admin =  admin;
        refreshPets();
        refreshUsers();
        refreshRegistrations();
        refreshIncidents();
    }

    private void refreshRegistrations() {
        regTable.getItems().setAll(
                custom.getAll(Registration.class, "client pet vet groomer")
        );
    }

    private void refreshPets() {
        petsTable.getItems().setAll(custom.getAll(Pet.class, "owner"));
    }
    private void refreshUsers() {
        List<Class<? extends User>> types = switch (userTypeFilter.getValue()) {
            case "Clients" -> List.of(Client.class);
            case "Staff" -> List.of(Vet.class, Groomer.class);
            case "Veterinarians" -> List.of(Vet.class);
            case "Groomers" -> List.of(Groomer.class);
            case "Admins" -> List.of(Admin.class);
            default -> List.of();
        };
        usersTable.getItems().setAll(custom.getUsersBytType(types));
    }
    private void refreshIncidents() {
        incidentsTable.getItems().setAll(custom.getAll(Incident.class, ""));
    }
}
