package com.example.demo2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.controller.RestaurantPageController;

import java.util.Stack;

public class Navigator {

    private static Stage mainStage;
    private static Scene mainScene;

    // 🔥 BACK STACK
    private static final Stack<String> history = new Stack<>();

    public static void setStage(Stage stage, Scene scene) {
        mainStage = stage;
        mainScene = scene;
    }

    /* ================= NORMAL NAV ================= */

    public static void goTo(String fxml) {
        try {
            saveCurrentView();

            Parent root = FXMLLoader.load(
                    Navigator.class.getResource(fxml)
            );

            // store fxml path on root
            root.setUserData(fxml);

            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= RESTAURANT NAV ================= */

    public static void goToRestaurant(int restaurantId) {
        try {
            saveCurrentView();

            FXMLLoader loader = new FXMLLoader(
                    Navigator.class.getResource(
                            "/com/example/demo2/restaurant-page.fxml")
            );

            Parent root = loader.load();
            RestaurantPageController controller = loader.getController();
            controller.setRestaurantId(restaurantId);

            root.setUserData("/com/example/demo2/restaurant-page.fxml");
            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= BACK ================= */

    public static void goBack() {
        if (history.isEmpty()) return;

        try {
            String previousFxml = history.pop();

            Parent root = FXMLLoader.load(
                    Navigator.class.getResource(previousFxml)
            );

            root.setUserData(previousFxml);
            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= INTERNAL ================= */

    private static void saveCurrentView() {
        if (mainScene != null && mainScene.getRoot() != null) {
            Object data = mainScene.getRoot().getUserData();
            if (data instanceof String fxml) {
                history.push(fxml);
            }
        }
    }
}