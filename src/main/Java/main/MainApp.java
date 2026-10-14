package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/veterinarija/login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Veterinarijos Klinika");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

/*
public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== VETERINARIJOS KLINIKOS SISTEMA ===\n");

        // 1. Sukuriame klienta ir veterinara¦
        Klientas klientas1 = new Klientas("Jonas Jonaitis", "+37060000000");
        Klientas klientas2 = new Klientas("Petkas Petkaitis", "+37060000300");
        Veterinaras gydytojas1 = new Veterinaras("Dr. Antanas", "Chirurgas");
        Veterinaras gydytojas2 = new Veterinaras("Dr. Dovydas", "Reabilitologas");

        // 2. Sukuriame augintini
        Augintinis augintinis1 = new Augintinis("Reksas", "Suo", 3, 15.6);
        Augintinis augintinis2 = new Augintinis("Margis", "Kapibara", 5, 82.2);
        Augintinis augintinis3 = new Augintinis("Miaukas", "Katinas", 13, 8.9);

        // 3. Priskiriame augintini klientui
        klientas1.uzregistruotiAugintini(augintinis1);
        klientas2.uzregistruotiAugintini(augintinis2);
        klientas2.uzregistruotiAugintini(augintinis3);

        System.out.println("\n--- Informacija apie augintini ---");
        System.out.println(augintinis1.gautiInfo());
        System.out.println("Seimininkas: " + augintinis1.getKlientas().getVardas());

        System.out.println(augintinis2.gautiInfo());
        System.out.println("Seimininkas: " + augintinis2.getKlientas().getVardas());

        System.out.println(augintinis3.gautiInfo());
        System.out.println("Seimininkas: " + augintinis3.getKlientas().getVardas());

        // 4. Sukuriame registracija vizitui
        System.out.println("\n--- Registracijos procesas ---");
        Registracija vizitas = klientas2.uzsakytiPaslauga("2026-10-15", "14:30", augintinis2, gydytojas2); //+nurodyti kokia paslauga


        // 5. Atliekame veiksmus su registracija
        vizitas.patvirtinti();
        gydytojas2.priimtiPacienta(vizitas);
    }
}
 */