package model.controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField; // ✅ NEW
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleBox;
    @FXML private CheckBox termsCheck;
    @FXML private Label messageLabel;
    @FXML private Button signupButton;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Egypt: 11 digits starting with 01
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^01\\d{9}$");

    private static final String BTN_NORMAL =
            "-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 12; -fx-padding: 12 14;";

    private static final String BTN_HOVER =
            "-fx-background-color: #FB923C; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 12; -fx-padding: 12 14;";

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Customer", "Restaurant Owner", "Delivery Staff");
        signupButton.setStyle(BTN_NORMAL);
    }

    @FXML
    private void onSignup() {
        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass  = passwordField.getText();
        String conf  = confirmPasswordField.getText();
        String role  = roleBox.getValue();

        // Basic validation
        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || phone.isEmpty()
                || pass.isEmpty() || conf.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Please enter a valid email.");
            return;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showError("Phone must be 11 digits and start with 01.");
            return;
        }

        if (!pass.equals(conf)) {
            showError("Passwords do not match.");
            return;
        }

        if (role == null) {
            showError("Please select a role.");
            return;
        }

        if (!termsCheck.isSelected()) {
            showError("You must agree to the terms.");
            return;
        }

        String fullName = first + " " + last;
        String passwordHash = sha256(pass);

        try {
            new UserDAO().createUser(fullName, email, passwordHash, role, phone);
            showSuccess("Account created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Signup failed. Please try again.");
        }
    }

    // ✅ Hover effects for Create account button
    @FXML
    private void signupHoverOn() {
        signupButton.setStyle(BTN_HOVER);
    }

    @FXML
    private void signupHoverOff() {
        signupButton.setStyle(BTN_NORMAL);
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
            showError("Cannot open Login page.");
        }
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #166534;");
        messageLabel.setText(msg);
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
