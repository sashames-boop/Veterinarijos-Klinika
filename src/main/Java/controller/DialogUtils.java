package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.function.Supplier;

final class DialogUtils {
    private DialogUtils() {}

    static <T> void dataValidation(Dialog<T> dialog, ButtonType submitButton,
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

    static void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
