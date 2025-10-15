package com.codeup.novabook.ui;

import com.codeup.novabook.domain.AccessLevel;
import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.infra.ServiceRegistry;
import com.codeup.novabook.service.IUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private ChoiceBox<UserRole> roleChoice;
    @FXML private ChoiceBox<AccessLevel> accessChoice;
    @FXML private Button createButton;
    @FXML private Button cancelButton;

    private final IUserService userService = ServiceRegistry.getInstance().userService();

    @FXML
    public void initialize() {
        roleChoice.getItems().addAll(UserRole.values());
        accessChoice.getItems().addAll(AccessLevel.values());
        roleChoice.setValue(UserRole.USER);
        accessChoice.setValue(AccessLevel.READ_ONLY);
    }

    @FXML
    public void onCreate(ActionEvent e) {
        try {
            User u = new User();
            u.setName(nameField.getText());
            u.setEmail(emailField.getText());
            u.setPassword(passwordField.getText());
            u.setPhone(phoneField.getText());
            u.setRole(roleChoice.getValue());
            u.setAccessLevel(accessChoice.getValue());
            userService.create(u);
            new Alert(Alert.AlertType.INFORMATION, "Usuario creado", ButtonType.OK).showAndWait();
            goToLogin();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void onCancel(ActionEvent e) { goToLogin(); }

    private void goToLogin() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/codeup/novabook/ui/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(getClass().getResource("/com/codeup/novabook/ui/styles.css").toExternalForm());
            stage.setTitle("NovaBook - Login");
            stage.setScene(scene);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}