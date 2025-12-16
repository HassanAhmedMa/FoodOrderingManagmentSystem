package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/com/example/demo2/hello-view.fxml")
        );

        Scene scene = new Scene(root);

        // 🔥 Give Navigator BOTH stage + scene
        Navigator.setStage(stage, scene);

        stage.setTitle("FoodHub");
        stage.setScene(scene);

        // 🔥 FULL SCREEN ONCE — NEVER AGAIN
        stage.setMaximized(true);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
