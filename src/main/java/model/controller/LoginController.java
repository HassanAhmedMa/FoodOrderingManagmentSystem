package model.controller;

import com.example.demo2.Navigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheck;
    @FXML private Label messageLabel;

    @FXML
    private void onLogin() {
        String email = emailField.getText().trim();
        String pass  = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter email and password.");
            return;
        }

        // UI-only test (no DB yet)
        Navigator.goTo("/com/example/demo2/hello-view.fxml");
    }

    @FXML
    private void onForgotPassword() {
        showError("Forgot password clicked.");
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #166534;");
        messageLabel.setText(msg);
    }

    @FXML
    private void goToSignup() {
        Navigator.goTo("/com/example/demo2/SignUp.fxml");
    }
}
