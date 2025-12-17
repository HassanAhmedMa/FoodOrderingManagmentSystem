package model.controller;

import dao.OrderDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.order.Order;
import model.state.DeliveredState;

public class DeliveryOrderCardController {

    // ================= FXML =================
    @FXML
    private Label orderIdLabel;

    @FXML
    private Label statusLabel;

    // ================= DATA =================
    private Order order;
    private DeliveryController parent;

    // ================= DAO =================
    private final OrderDAO orderDAO = new OrderDAO();

    // ================= SETTERS =================
    public void setOrder(Order order) {
        this.order = order;
        orderIdLabel.setText("Order #" + order.getId());
        statusLabel.setText(order.getStatus());
    }

    public void setParent(DeliveryController parent) {
        this.parent = parent;
    }

    // ================= ACTION =================
    @FXML
    private void onDelivered() {

        // 🔐 role-based update
        boolean success = orderDAO.updateOrderStatus(
                order.getId(),
                "DELIVERED",
                "DELIVERY"
        );

        if (!success) {
            return;
        }

        // ✅ update in-memory state (State pattern)
        order.setState(new DeliveredState());

        // 🔄 refresh delivery dashboard
        parent.refreshOrders();
    }
}
