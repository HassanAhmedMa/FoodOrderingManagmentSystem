package model.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.order.OrderItem;

public class CartItemCardController {

    @FXML private Label foodName;
    @FXML private Label restaurantName;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private ImageView foodImage;

    private OrderItem orderItem;
    private CartController cartController;
    private HBox root;

    public void init(OrderItem orderItem, CartController cartController, HBox root) {
        this.orderItem = orderItem;
        this.cartController = cartController;
        this.root = root;

        foodName.setText(orderItem.getItem().getName());
        priceLabel.setText(String.format("%.2f$", orderItem.getItem().getPrice()));
        quantityLabel.setText(String.valueOf(orderItem.getQuantity()));
    }

    @FXML
    private void onIncrease() {
        if (orderItem == null || cartController == null) return;
        orderItem.increaseQuantity();
        quantityLabel.setText(String.valueOf(orderItem.getQuantity()));
        cartController.recalculate();
    }

    @FXML
    private void onDecrease() {
        if (orderItem == null || cartController == null) return;
        if (orderItem.getQuantity() > 1) {
            orderItem.decreaseQuantity();
            quantityLabel.setText(String.valueOf(orderItem.getQuantity()));
            cartController.recalculate();
        } else {
            cartController.removeItem(orderItem, root);
        }
    }

    @FXML
    private void onDelete() {
        if (orderItem == null || cartController == null) return;
        cartController.removeItem(orderItem, root);
    }
}
