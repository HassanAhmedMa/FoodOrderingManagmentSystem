package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.user.User;
import model.validation.login.*;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        clearMessage();
    }

    @FXML
    private void onLogin() {
        clearMessage();
        clearMarks();

        LoginRequest r = new LoginRequest();
        r.email = emailField.getText().trim();
        r.password = passwordField.getText();

        LoginHandler chain = new EmailValidationHandler();
        chain.setNext(new PasswordRequiredHandler())
                .setNext(new UserExistsHandler(userDAO))
                .setNext(new PasswordMatchesHandler());

        try {
            chain.handle(r);

            User user = userDAO.getUserByEmail(r.email);
            Session.setUser(user);
            if(user.getRole().equals("RESTAURANT")) {
                Navigator.goTo("/com/example/demo2/restaurant-dashboard.fxml");
            }
            else if(user.getRole().equals("DELIVERY"))
            {
                Navigator.goTo("/com/example/demo2/delivery-dashboard.fxml");
            }
            else{
                Navigator.goTo("/com/example/demo2/hello-view.fxml");
            }


        } catch (LoginValidationException ex) {
            markInvalidByKey(ex.getFieldKey());
            showError(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Login failed. Please try again.");
        }
    }

    @FXML
    private void goToSignup() {
        Navigator.goTo("/com/example/demo2/SignUp.fxml");
    }

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
            case "email" -> markInvalid(emailField);
            case "password" -> markInvalid(passwordField);
            default -> { }
        }
    }

    private void clearMarks() {
        if (emailField != null) emailField.setStyle("");
        if (passwordField != null) passwordField.setStyle("");
    }
}