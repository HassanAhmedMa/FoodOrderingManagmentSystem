package model.controller;

import com.example.demo2.Navigator;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class SignUpController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleBox;
    @FXML private CheckBox termsCheck;
    @FXML private Label messageLabel;
    @FXML private Button signupButton;

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Egypt: 11 digits starting with 01
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^01\\d{9}$");

    // 8+ chars, at least 1 uppercase, 1 lowercase, 1 digit
    private static final Pattern PASS_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    // Button styles
    private static final String BTN_NORMAL =
            "-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: bold;" +
                    "-fx-background-radius: 12; -fx-padding: 12 14;";

    private static final String BTN_HOVER =
            "-fx-background-color: #FB923C; -fx-text-fill: white; -fx-font-weight: bold;" +
                    "-fx-background-radius: 12; -fx-padding: 12 14;";

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Customer", "Restaurant Owner", "Delivery Staff");
        if (signupButton != null) signupButton.setStyle(BTN_NORMAL);
        clearMessage();
    }

    @FXML
    private void onSignup() {
        clearMarks();
        clearMessage();

        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass  = passwordField.getText();
        String conf  = confirmPasswordField.getText();
        String role  = roleBox.getValue();

        // ---- Validation (with red borders) ----
        if (first.isEmpty()) { markInvalid(firstNameField); showError("First name is required."); return; }
        if (last.isEmpty())  { markInvalid(lastNameField);  showError("Last name is required.");  return; }

        if (email.isEmpty()) { markInvalid(emailField); showError("Email is required."); return; }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            markInvalid(emailField);
            showError("Please enter a valid email.");
            return;
        }

        if (phone.isEmpty()) { markInvalid(phoneField); showError("Phone number is required."); return; }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            markInvalid(phoneField);
            showError("Phone must be 11 digits and start with 01.");
            return;
        }

        if (pass.isEmpty()) { markInvalid(passwordField); showError("Password is required."); return; }
        if (!PASS_PATTERN.matcher(pass).matches()) {
            markInvalid(passwordField);
            showError("Password must be 8+ chars with uppercase, lowercase, and number.");
            return;
        }

        if (conf.isEmpty()) { markInvalid(confirmPasswordField); showError("Please confirm your password."); return; }
        if (!pass.equals(conf)) {
            markInvalid(confirmPasswordField);
            showError("Passwords do not match.");
            return;
        }

        if (role == null) { markInvalid(roleBox); showError("Please select a role."); return; }

        if (!termsCheck.isSelected()) {
            showError("You must agree to the terms.");
            return;
        }

        // ---- Duplicate checks (DB) ----
        if (userDAO.emailExists(email)) {
            markInvalid(emailField);
            showError("This email is already registered.");
            return;
        }

        if (userDAO.phoneExists(phone)) {
            markInvalid(phoneField);
            showError("This phone number is already registered.");
            return;
        }

        // ---- Save user (DB) ----
        String fullName = first + " " + last;

        try {
            // NOTE: pass stored in password_hash column (plain text currently)
            userDAO.createUser(fullName, email, pass, role, phone);
            Navigator.goTo("/com/example/demo2/Login.fxml");

            // Optional: switch to login page automatically
            // goToLogin();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Signup failed. Please try again.");
        }
    }

    // Hover effects (Button)
    @FXML
    private void signupHoverOn() {
        if (signupButton != null) signupButton.setStyle(BTN_HOVER);
    }

    @FXML
    private void signupHoverOff() {
        if (signupButton != null) signupButton.setStyle(BTN_NORMAL);
    }

    @FXML
    private void goToLogin() {
        Navigator.goTo("/com/example/demo2/Login.fxml");
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
        if (firstNameField != null) firstNameField.setStyle("");
        if (lastNameField != null) lastNameField.setStyle("");
        if (emailField != null) emailField.setStyle("");
        if (phoneField != null) phoneField.setStyle("");
        if (passwordField != null) passwordField.setStyle("");
        if (confirmPasswordField != null) confirmPasswordField.setStyle("");
        if (roleBox != null) roleBox.setStyle("");
    }
}
