package com.codeup.novabook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Novabook extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        com.codeup.novabook.infra.LogConfig.configure();
        
        // Adjusted path: use relative path "ui/LoginView.fxml" based on this class package (com.codeup.novabook)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            
            // Adjusted path for CSS
            scene.getStylesheets().add(getClass().getResource("ui/styles.css").toExternalForm());
            
            stage.setTitle("NovaBook - Login");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            // Basic handling if FXML or CSS file is not found
            System.err.println("Error loading FXML or CSS. Verify location at src/main/java/com/codeup/novabook/ui/");
            e.printStackTrace();
            throw e; // Rethrow to stop the application if necessary
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
