package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Locale;

import static java.util.regex.Pattern.matches;

public class TableSearch {
    private TableSearch() {}

    public static <T> void setup(TableView<T> table, TextField searchField, ObservableList<T> data) {

        FilteredList<T> filteredData = new FilteredList<>(data,
                item -> matches(table, item, searchField.getText()));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> matches(table, item, newValue));
        });

        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }

    private static <T> boolean matches(TableView<T> table, T item, String text) {

        String searchText = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);

        if (searchText.isEmpty()) {
            return true;
        }

        for (TableColumn<T, ?> column : table.getVisibleLeafColumns()) {
            Object value = column.getCellData(item);

            if(value != null &&
            value.toString().toLowerCase(Locale.ROOT).contains(searchText)) {
                return true;
            }
        }

        return false;
    }
}
