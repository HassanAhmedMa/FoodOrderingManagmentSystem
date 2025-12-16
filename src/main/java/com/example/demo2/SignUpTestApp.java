package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpTestApp extends Application {

    // change this to "SignUp.fxml" or "Login.fxml"
    private static final String START_PAGE = "SignUp.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demo2/" + START_PAGE)
        );

        Scene scene = new Scene(loader.load(), 1100, 650);
        stage.setTitle("Test - " + START_PAGE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
