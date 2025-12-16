package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demo2/cart-page.fxml")
        );

        Parent root = loader.load();

        // 🔥 THIS LINE IS THE FIX
        root.setUserData("/com/example/demo2/SignUp.fxml");

        Scene scene = new Scene(root);

        // Give Navigator BOTH stage + scene
        Navigator.setStage(stage, scene);

        stage.setTitle("FoodHub");
        stage.setScene(scene);

        stage.setMaximized(true);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
