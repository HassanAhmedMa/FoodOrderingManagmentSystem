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

    // Back stack stores navigation state
    private static final Stack<Object> history = new Stack<>();

    public static void setStage(Stage stage, Scene scene) {
        mainStage = stage;
        mainScene = scene;
    }

    /* ================= GENERIC NAV ================= */

    public static void goTo(String fxml) {
        try {
            saveCurrentView();

            Parent root = FXMLLoader.load(
                    Navigator.class.getResource(fxml)
            );

            root.setUserData(fxml);
            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= RESTAURANT NAV (🔥 REQUIRED) ================= */

    public static void goToRestaurant(int restaurantId) {
        try {
            saveCurrentView();

            FXMLLoader loader = new FXMLLoader(
                    Navigator.class.getResource("/com/example/demo2/restaurant-page.fxml")
            );

            Parent root = loader.load();

            RestaurantPageController controller = loader.getController();
            controller.setRestaurantId(restaurantId); // 🔥 REQUIRED

            // store both fxml + restaurantId
            root.setUserData(new RestaurantNavState(
                    "/com/example/demo2/restaurant-page.fxml",
                    restaurantId
            ));

            mainScene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= BACK ================= */

    public static void goBack() {
        if (history.isEmpty()) return;

        try {
            Object state = history.pop();

            if (state instanceof RestaurantNavState rs) {

                FXMLLoader loader = new FXMLLoader(
                        Navigator.class.getResource(rs.fxml)
                );

                Parent root = loader.load();
                RestaurantPageController controller = loader.getController();
                controller.setRestaurantId(rs.restaurantId); // 🔥 RESTORE ID

                root.setUserData(rs);
                mainScene.setRoot(root);

            } else if (state instanceof String fxml) {

                Parent root = FXMLLoader.load(
                        Navigator.class.getResource(fxml)
                );

                root.setUserData(fxml);
                mainScene.setRoot(root);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= INTERNAL ================= */

    private static void saveCurrentView() {
        if (mainScene != null && mainScene.getRoot() != null) {
            Object data = mainScene.getRoot().getUserData();
            if (data != null) {
                history.push(data);
            }
        }
    }

    /* ================= STATE CLASS ================= */

    private static class RestaurantNavState {
        String fxml;
        int restaurantId;

        RestaurantNavState(String fxml, int restaurantId) {
            this.fxml = fxml;
            this.restaurantId = restaurantId;
        }
    }
}
