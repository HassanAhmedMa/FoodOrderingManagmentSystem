package model.controller;

import dao.OrderDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.order.Order;

public class RestaurantOrderCardController {

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button actionButton;

    private Order order;
    private RestaurantDashboardController parent;

    private final OrderDAO orderDAO = new OrderDAO();

    // ================= SETTERS =================

    public void setOrder(Order order) {
        this.order = order;

        orderIdLabel.setText("Order #" + order.getId());
        statusLabel.setText(order.getStatus());

        configureButton();
    }

    public void setParent(RestaurantDashboardController parent) {
        this.parent = parent;
    }

    // ================= UI LOGIC =================

    private void configureButton() {

        actionButton.setVisible(true);

        switch (order.getStatus()) {

            case "PLACED" -> actionButton.setText("Confirm");

            case "CONFIRMED" -> actionButton.setText("Prepare");

            case "PREPARING" -> actionButton.setText("Order Ready");

            default -> actionButton.setVisible(false);
        }
    }

    // ================= ACTION =================

    @FXML
    private void onNextStatus() {

        String current = order.getStatus();

        // ===============================
        // PREPARING → OUT_FOR_DELIVERY
        // ===============================
        if ("PREPARING".equals(current)) {

            boolean assigned =
                    orderDAO.assignOrderAutomatically(order.getId());

            if (!assigned) {
                parent.showError(
                        "No delivery staff available (max 3 active orders)."
                );
                return;
            }

            parent.refreshOrders();
            return;
        }

        // ===============================
        // PLACED → CONFIRMED → PREPARING
        // ===============================
        String nextStatus = switch (current) {
            case "PLACED" -> "CONFIRMED";
            case "CONFIRMED" -> "PREPARING";
            default -> null;
        };

        if (nextStatus == null) return;

        orderDAO.updateOrderStatus(
                order.getId(),
                nextStatus,
                "RESTAURANT"
        );

        parent.refreshOrders();
    }

}
