package model.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleBox;
    @FXML private CheckBox termsCheck;
    @FXML private Label messageLabel;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASS_REGEX  = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Customer", "Restaurant Owner", "Delivery Staff");
    }

    @FXML
    private void onSignup() {
        clearMarks();

        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String pass  = passwordField.getText();
        String conf  = confirmPasswordField.getText();
        String role  = roleBox.getValue();

        if (first.isEmpty()) { markInvalid(firstNameField); showError("First name is required."); return; }
        if (last.isEmpty())  { markInvalid(lastNameField);  showError("Last name is required.");  return; }
        if (email.isEmpty()) { markInvalid(emailField);     showError("Email is required.");      return; }
        if (!email.matches(EMAIL_REGEX)) { markInvalid(emailField); showError("Please enter a valid email."); return; }

        if (pass.isEmpty())  { markInvalid(passwordField);  showError("Password is required.");   return; }
        if (!pass.matches(PASS_REGEX)) {
            markInvalid(passwordField);
            showError("Password must be 8+ chars with 1 uppercase, 1 lowercase, and 1 number.");
            return;
        }

        if (conf.isEmpty()) { markInvalid(confirmPasswordField); showError("Please confirm your password."); return; }
        if (!pass.equals(conf)) { markInvalid(confirmPasswordField); showError("Passwords do not match."); return; }

        if (role == null) { markInvalid(roleBox); showError("Please select a role."); return; }

        if (!termsCheck.isSelected()) { showError("You must agree to the terms."); return; }

        showSuccess("Signup controller works");
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #166534;");
        messageLabel.setText(msg);
    }

    private void markInvalid(Control c) {
        c.setStyle(c.getStyle() + "; -fx-border-color: #ef4444; -fx-border-width: 2; -fx-border-radius: 10;");
    }

    private void clearMarks() {
        firstNameField.setStyle("");
        lastNameField.setStyle("");
        emailField.setStyle("");
        passwordField.setStyle("");
        confirmPasswordField.setStyle("");
        roleBox.setStyle("");
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo2/Login.fxml")
            );
            Scene scene = new Scene(loader.load(), 1100, 650);

            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
