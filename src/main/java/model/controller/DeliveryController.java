package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.OrderDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.order.Order;
import model.user.User;

import java.util.List;

public class DeliveryController {

    public Label loggedInUser;
    // ================= FXML =================
    @FXML
    private VBox ordersContainer;

    // ================= DAO =================
    private final OrderDAO orderDAO = new OrderDAO();

    // ================= INIT =================
    @FXML
    public void initialize() {

        User user = Session.getUser();
        loggedInUser.setText(user.getFullName());
        // 🔐 allow only delivery staff
        if (user == null || !"DELIVERY".equalsIgnoreCase(user.getRole())) {
            return;
        }

        loadOrders();
    }

    // ================= LOAD ORDERS =================
    private void loadOrders() {

        ordersContainer.getChildren().clear();

        int deliveryId = Session.getUser().getId();

        List<Order> orders =
                orderDAO.getOrdersByDeliveryStaff(deliveryId);

        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/example/demo2/delivery-order-card.fxml"
                        )
                );

                ordersContainer.getChildren().add(loader.load());

                DeliveryOrderCardController controller =
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

    public void onLogoutClicked(ActionEvent actionEvent) {
        Session.setUser(null);
        Navigator.goTo("/com/example/demo2/Login.fxml");
    }
}
