package model.controller;

import com.example.demo2.Navigator;
import dao.OrderDAO;
import com.example.demo2.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import model.order.Order;
import model.user.Customer;

import java.util.List;

public class MyOrdersController {

    @FXML
    private VBox ordersContainer;

    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {

        Customer customer = (Customer) Session.getUser();
        if (customer == null) {
            System.out.println("❌ No logged-in customer in session");
            return;
        }

        System.out.println("✅ Logged in customer ID: " + customer.getId());

        List<Order> orders = orderDAO.getOrdersByCustomer(customer.getId());
        System.out.println("📦 Orders found: " + orders.size());

        ordersContainer.getChildren().clear();

        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/demo2/order-item.fxml")
                );

                // 🔥 THIS IS THE FIX
                Node card = loader.load();

                OrderItemController controller = loader.getController();
                controller.setOrder(order);

                ordersContainer.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleBack(){
        Navigator.goTo("/com/example/demo2/browse-restaurants.fxml");
    }


}
