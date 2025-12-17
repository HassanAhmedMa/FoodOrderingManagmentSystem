package model.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.order.Order;
import model.order.OrderItem;

public class OrderItemController {

    private static final double DELIVERY_FEE = 2.99;

    @FXML private Label restaurantNameLabel;
    @FXML private Label orderMetaLabel;
    @FXML private Label statusLabel;
    @FXML private Label totalLabel;
    @FXML private VBox itemsContainer;

    public void setOrder(Order order) {

        restaurantNameLabel.setText(order.getRestaurant().getName());
        statusLabel.setText(order.getStatus());

        double itemsTotal = order.calculateTotal();
        double finalTotal = itemsTotal + DELIVERY_FEE;

        totalLabel.setText(String.format("%.2f $", finalTotal));

        orderMetaLabel.setText(order.getItems().size() + " items");

        itemsContainer.getChildren().clear();

        for (OrderItem item : order.getItems()) {
            Label row = new Label(
                    item.getItem().getName() +
                            " x" + item.getQuantity() +
                            " - " +
                            String.format("%.2f$", item.getTotalPrice())
            );
            itemsContainer.getChildren().add(row);
        }

        // Optional: show delivery fee as a line item
        Label deliveryRow = new Label(
                "Delivery Fee - " + String.format("%.2f$", DELIVERY_FEE)
        );
        deliveryRow.setStyle("-fx-text-fill: #777;");
        itemsContainer.getChildren().add(deliveryRow);
    }
}
