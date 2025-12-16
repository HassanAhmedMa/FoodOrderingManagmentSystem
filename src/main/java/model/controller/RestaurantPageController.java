package model.controller;

import com.example.demo2.Navigator;
import dao.MenuItemDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.restaurant.MenuItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantPageController implements Initializable {

    /* ================= FXML ================= */

    @FXML
    private FlowPane menuFlowPane;

    @FXML
    private Button backButton;

    /* ================= DAO ================= */

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    // TEMP: existing restaurant in your DB
    private final int restaurantId = 1;

    /* ================= INIT ================= */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMenuItems();
    }

    /* ================= MENU ================= */

    private void loadMenuItems() {

        List<MenuItem> items =
                menuItemDAO.getMenuItemsByRestaurant(restaurantId);

        menuFlowPane.getChildren().clear();

        for (MenuItem item : items) {
            menuFlowPane.getChildren().add(createFoodCard(item));
        }
    }

    private VBox createFoodCard(MenuItem item) {

        VBox card = new VBox(8);
        card.getStyleClass().add("food-card");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            imageView.setImage(new Image(item.getImageUrl(), true));
        }

        Label title = new Label(item.getName());
        title.getStyleClass().add("food-title");

        Label desc = new Label(item.getDescription());
        desc.getStyleClass().add("food-desc");

        Label price = new Label(String.valueOf(item.getPrice()));
        price.getStyleClass().add("food-price");

        Button addBtn = new Button("+ Add");
        addBtn.getStyleClass().add("add-btn");

        HBox bottom = new HBox(10, price, addBtn);
        bottom.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(imageView, title, desc, bottom);

        return card;
    }

    /* ================= NAVIGATION ================= */

    @FXML
    private void handleBack() {
        Navigator.goTo("/com/example/demo2/hello-view.fxml");
    }
}
