package model.controller;

import com.example.demo2.Session;
import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.order.Order;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.user.User;

import java.util.List;

public class RestaurantDashboardController {

    // ================= FXML =================
    @FXML
    private VBox menuItemsContainer;

    @FXML
    private Label restaurantNameLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    private VBox ordersContainer;

    // ================= DAO =================
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    // ================= DATA =================
    private Restaurant restaurant;

    // ================= INIT =================
    @FXML
    public void initialize() {

        User user = Session.getUser();
        if (user == null) return;

        restaurant = restaurantDAO.getRestaurantByOwner(user.getId());
        if (restaurant == null) return;

        // Load restaurant name
        restaurantNameLabel.setText(restaurant.getName());

        // Load statistics
        int totalOrders = orderDAO.getTotalOrdersByRestaurant(restaurant.getId());
        totalOrdersLabel.setText(String.valueOf(totalOrders));

        // TODO: replace with real revenue calculation
        revenueLabel.setText("45.84");

        loadMenuItems();
        loadOrders();
    }

    // ================= MENU =================
    private void loadMenuItems() {

        menuItemsContainer.getChildren().clear();

        List<MenuItem> items =
                menuItemDAO.getMenuItemsByRestaurant(restaurant.getId());

        for (MenuItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/example/demo2/restaurant-menu-item-card.fxml"
                        )
                );

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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    // ================= ADD MENU ITEM =================
    @FXML
    private void onAddItem() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo2/add-menu-item.fxml")
            );

            Parent root = loader.load();

            AddMenuItemController controller = loader.getController();
            controller.setParent(this);

            Stage stage = new Stage();
            stage.setTitle("Add Menu Item");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // ================= ORDERS =================
    private void loadOrders() {

        ordersContainer.getChildren().clear();

        List<Order> orders =
                orderDAO.getOrdersByRestaurant(restaurant.getId());

        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/example/demo2/restaurant-order-card.fxml"
                        )
                );

                ordersContainer.getChildren().add(loader.load());

                RestaurantOrderCardController controller =
                        loader.getController();

                controller.setOrder(order);
                controller.setParent(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ================= REFRESH =================
    public void refreshOrders() {
        loadOrders();
    }

    public void refreshMenu() {
        loadMenuItems();
    }

    // ================= REMOVED / UNUSED =================
    /*
     * ❌ REMOVED METHOD:
     *
     * onOrderReady(Order order)
     *
     * Reason:
     * - Not connected to FXML
     * - Delivery assignment is handled in RestaurantOrderCardController
     * - Keeping it causes duplicate logic and bugs
     *
     * Delivery logic MUST stay in:
     * RestaurantOrderCardController + DAO
     */

    /*
     * ❌ REMOVED METHOD:
     *
     * onNextStatus(Order order)
     *
     * Reason:
     * - Status progression is handled per-order card
     * - Dashboard should not directly mutate order state
     */
}
