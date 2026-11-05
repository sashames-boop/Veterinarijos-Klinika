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

public class UsersController {
    private CustomHibernate custom;
    private Admin admin;

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
    @FXML
    private TextField searchField;
    private final ObservableList<User> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        custom = MainApp.custom;
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

        TableSearch.setup(usersTable, searchField, data);
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

    void setAdmin(Admin admin) {
        this.admin = admin;
    }

    void refreshUsers() {
        List<Class<? extends User>> types = switch (userTypeFilter.getValue()) {
            case "Clients" -> List.of(Client.class);
            case "Staff" -> List.of(Vet.class, Groomer.class);
            case "Veterinarians" -> List.of(Vet.class);
            case "Groomers" -> List.of(Groomer.class);
            case "Admins" -> List.of(Admin.class);
            default -> List.of();
        };
        data.setAll(custom.getUsersBytType(types));
    }
}
