package model.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.order.Order;
import model.order.OrderItem;

public class OrderItemController {

    @FXML private Label restaurantNameLabel;
    @FXML private Label orderMetaLabel;
    @FXML private Label statusLabel;
    @FXML private Label totalLabel;
    @FXML private VBox itemsContainer;

    public void setOrder(Order order) {

        restaurantNameLabel.setText(order.getRestaurant().getName());
        statusLabel.setText(order.getStatus());
        totalLabel.setText(order.calculateTotal() + " $");

        orderMetaLabel.setText(order.getItems().size() + " items");

        itemsContainer.getChildren().clear();

        for (OrderItem item : order.getItems()) {
            Label row = new Label(
                    item.getItem().getName() +
                            " x" + item.getQuantity() +
                            " - " + item.getTotalPrice() + "$"
            );
            itemsContainer.getChildren().add(row);
        }
    }
}
