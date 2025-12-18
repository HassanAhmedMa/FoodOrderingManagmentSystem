package model.controller;

import com.example.demo2.Navigator;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.validation.signup.*;

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

        // Build request from UI
        SignupRequest r = new SignupRequest();
        r.firstName = firstNameField.getText().trim();
        r.lastName = lastNameField.getText().trim();
        r.email = emailField.getText().trim();
        r.phone = phoneField.getText().trim();
        r.password = passwordField.getText();
        r.confirmPassword = confirmPasswordField.getText();
        r.role = switch (roleBox.getValue()) {
            case "Customer" -> "CUSTOMER";
            case "Restaurant Owner" -> "RESTAURANT";
            case "Delivery Staff" -> "DELIVERY";
            default -> null;
        };
        r.acceptedTerms = termsCheck.isSelected();

        SignupHandler chain = new NameValidationHandler();
        chain.setNext(new EmailValidationHandler())
                .setNext(new PhoneValidationHandler())
                .setNext(new PasswordValidationHandler())
                .setNext(new RoleValidationHandler())
                .setNext(new TermsValidationHandler())
                .setNext(new DuplicateEmailHandler(userDAO))
                .setNext(new DuplicatePhoneHandler(userDAO));



        try {
            chain.handle(r);

            // Save user if everything passed
            String fullName = r.firstName + " " + r.lastName;
            userDAO.createUser(fullName, r.email, r.password, r.role, r.phone);


            Navigator.goTo("/com/example/demo2/Login.fxml");

        } catch (SignupValidationException ex) {
            markInvalidByKey(ex.getFieldKey());
            showError(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Signup failed. Please try again.");
        }
    }

    // Hover effects
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

    // ---- UI helpers ----
    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setText(msg);
    }

    private void clearMessage() {
        if (messageLabel != null) messageLabel.setText("");
    }

    private void markInvalid(Control c) {
        String base = c.getStyle() == null ? "" : c.getStyle();
        c.setStyle(base + "; -fx-border-color: #ef4444; -fx-border-width: 2; -fx-border-radius: 10;");
    }

    private void markInvalidByKey(String key) {
        switch (key) {
            case "firstName" -> markInvalid(firstNameField);
            case "lastName" -> markInvalid(lastNameField);
            case "email" -> markInvalid(emailField);
            case "phone" -> markInvalid(phoneField);
            case "password" -> markInvalid(passwordField);
            case "confirmPassword" -> markInvalid(confirmPasswordField);
            case "role" -> markInvalid(roleBox);
            case "terms" -> { /* message is enough for checkbox */ }
            default -> { /* ignore */ }
        }
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
