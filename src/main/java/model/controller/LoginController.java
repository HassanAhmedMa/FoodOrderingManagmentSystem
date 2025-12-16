package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.user.User;

import java.util.regex.Pattern;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @FXML
    private void initialize() {
        clearMessage();
    }

    @FXML
    private void onLogin() {
        clearMessage();
        clearMarks();

        String email = emailField.getText().trim();
        String pass  = passwordField.getText();

        if (email.isEmpty()) { markInvalid(emailField); showError("Email is required."); return; }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            markInvalid(emailField);
            showError("Please enter a valid email.");
            return;
        }
        if (pass.isEmpty()) { markInvalid(passwordField); showError("Password is required."); return; }

        // ---- DB lookup ----
        UserDAO.UserLoginData data = userDAO.getLoginDataByEmail(email);

        if (data == null) {
            markInvalid(emailField);
            showError("No account found with this email.");
            return;
        }

        // ---- Password check (plain text currently) ----
        if (!pass.equals(data.getPassword())) {
            markInvalid(passwordField);
            showError("Incorrect password.");
            return;
        }

//        showSuccess("Welcome " + data.getFullName() + " (" + data.getRole() + ")");
        User user = userDAO.getUserByEmail(email);
        Session.setUser(user);
        Navigator.goTo("/com/example/demo2/hello-view.fxml");
        // Next step: redirect by role
        // e.g. if role == Customer -> open customer dashboard
    }

    @FXML
    private void goToSignup() {
        Navigator.goTo("/com/example/demo2/SignUp.fxml");
    }

    // ---- UI Helpers ----
    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #166534;");
        messageLabel.setText(msg);
    }

    private void clearMessage() {
        if (messageLabel != null) messageLabel.setText("");
    }

    private void markInvalid(Control c) {
        String base = c.getStyle() == null ? "" : c.getStyle();
        c.setStyle(base + "; -fx-border-color: #ef4444; -fx-border-width: 2; -fx-border-radius: 10;");
    }

    private void clearMarks() {
        if (emailField != null) emailField.setStyle("");
        if (passwordField != null) passwordField.setStyle("");
    }
}
