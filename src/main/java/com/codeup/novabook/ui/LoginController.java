package com.codeup.novabook.ui;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.infra.ServiceRegistry;
import com.codeup.novabook.infra.SessionManager;
import com.codeup.novabook.service.IUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    private final IUserService userService = ServiceRegistry.getInstance().userService();

    @FXML
    public void onLogin(ActionEvent e) {
        String email = emailField.getText();
        String pass = passwordField.getText();
        try {
            Optional<User> user = userService.authenticate(email, pass);
            if (user.isPresent()) {
                SessionManager.setCurrentUser(user.get());
                // Navigate to main view
                Stage stage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/codeup/novabook/ui/MainView.fxml"));
                Scene scene = new Scene(loader.load(), 1100, 700);
                scene.getStylesheets().add(getClass().getResource("/com/codeup/novabook/ui/styles.css").toExternalForm());
                stage.setTitle("NovaBook");
                stage.setScene(scene);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid credentials", ButtonType.OK).showAndWait();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void onOpenRegister(ActionEvent e) {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/codeup/novabook/ui/RegisterView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            scene.getStylesheets().add(getClass().getResource("/com/codeup/novabook/ui/styles.css").toExternalForm());
            stage.setTitle("NovaBook - Register");
            stage.setScene(scene);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}