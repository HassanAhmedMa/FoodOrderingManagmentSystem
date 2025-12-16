package model.controller;

import com.example.demo2.Session;
import dao.MenuItemDAO;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.user.User;

import java.util.List;

public class RestaurantDashboardController {

    @FXML
    private VBox menuItemsContainer;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    // ✅ MUST be a field
    private Restaurant restaurant;

    @FXML
    public void initialize() {
        User user = Session.getUser();
        if (user == null) return;

        // ✅ assign to field, not local variable
        restaurant = restaurantDAO.getRestaurantByOwner(user.getId());
        if (restaurant == null) return;

        loadMenuItems();
    }

    private void loadMenuItems() {
        menuItemsContainer.getChildren().clear();

        List<MenuItem> items =
                menuItemDAO.getMenuItemsByRestaurant(restaurant.getId());

        for (MenuItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/demo2/restaurant-menu-item-card.fxml")
                );

                // ✅ root is HBox (matches FXML)
                HBox card = loader.load();

                RestaurantMenuItemCardController controller =
                        loader.getController();

                controller.setItem(item);
                controller.setParent(this);

                menuItemsContainer.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🔁 used by edit/delete
    public void refreshMenu() {
        loadMenuItems();
    }
}
