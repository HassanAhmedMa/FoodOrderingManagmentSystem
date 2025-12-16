package com.example.demo2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    private static Stage mainStage;
    private static Scene mainScene;

    public static void setStage(Stage stage, Scene scene) {
        mainStage = stage;
        mainScene = scene;
    }

    public static void goTo(String fxml) {
        try {
            Parent root = FXMLLoader.load(
                    Navigator.class.getResource(fxml)
            );

            // 🔥 Swap ROOT, not Scene
            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
