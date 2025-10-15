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
        
        // 🚨 RUTA AJUSTADA: Usamos la ruta relativa "ui/LoginView.fxml" 
        // respecto al paquete de la clase Novabook (com.codeup.novabook).
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            
            // 🚨 RUTA AJUSTADA para el CSS
            scene.getStylesheets().add(getClass().getResource("ui/styles.css").toExternalForm());
            
            stage.setTitle("NovaBook - Login");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            // Manejo básico de la excepción si el archivo sigue sin encontrarse
            System.err.println("Error al cargar el FXML o CSS. Verifique la ubicación en src/main/java/com/codeup/novabook/ui/");
            e.printStackTrace();
            throw e; // Relanza la excepción para detener la aplicación si es necesario
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
