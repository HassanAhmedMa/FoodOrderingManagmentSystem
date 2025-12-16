package model.controller;

import dao.OrderDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.order.Order;
import model.order.OrderItem;
import model.payment.*;
import model.restaurant.MenuItem;

public class CartController {

    @FXML private VBox orderSummary;
    @FXML private VBox cartItemsBox;
    @FXML private Button clearCartButton;

    private TextField addressField;
    private Button cardButton, cashButton, placeOrderButton;
    private Label subtotalLabel, taxLabel, totalLabel;

    private Order currentOrder;
    private PaymentStrategy paymentStrategy;

    private static final double TAX_RATE = 0.10;
    private static final double DELIVERY_FEE = 2.99;

    @FXML
    public void initialize() {

        addressField = (TextField) orderSummary.lookup("#addressField");
        cardButton = (Button) orderSummary.lookup("#cardButton");
        cashButton = (Button) orderSummary.lookup("#cashButton");
        placeOrderButton = (Button) orderSummary.lookup("#placeOrderButton");
        subtotalLabel = (Label) orderSummary.lookup("#subtotalLabel");
        taxLabel = (Label) orderSummary.lookup("#taxLabel");
        totalLabel = (Label) orderSummary.lookup("#totalLabel");

        disablePlaceOrder();

        cardButton.setOnAction(e -> selectCard());
        cashButton.setOnAction(e -> selectCash());
        clearCartButton.setOnAction(e -> clearCart());
        placeOrderButton.setOnAction(e -> placeOrder());

        addressField.textProperty().addListener((a,b,c) -> validate());

        createDemoOrder();
    }

    private void createDemoOrder() {
        currentOrder = new Order(1, null, null);
        MenuItem pizza = new MenuItem(1, "Margherita Pizza", 14.99);
        currentOrder.addItem(new OrderItem(pizza, 1));
        currentOrder.addItem(new OrderItem(pizza, 1));
        recalculate();
    }

    private void selectCard() {
        paymentStrategy = new CardPayment();
        highlight(cardButton, cashButton);
        validate();
    }

    private void selectCash() {
        paymentStrategy = new CashPayment();
        highlight(cashButton, cardButton);
        validate();
    }

    private void highlight(Button active, Button inactive) {
        active.setStyle("-fx-background-color:#ff6a00;-fx-text-fill:white;-fx-background-radius:10;");
        inactive.setStyle("-fx-background-color:white;-fx-border-color:#ddd;-fx-background-radius:10;");
    }

    private void recalculate() {
        double subtotal = currentOrder.calculateTotal();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax + DELIVERY_FEE;

        subtotalLabel.setText("Subtotal " + String.format("%.2f$", subtotal));
        taxLabel.setText("Tax " + String.format("%.2f$", tax));
        totalLabel.setText("Total " + String.format("%.2f$", total));
        validate();
    }

    private void validate() {
        if (currentOrder.isEmpty() || paymentStrategy == null || addressField.getText().isBlank()) {
            disablePlaceOrder();
        } else {
            enablePlaceOrder();
        }
    }

    private void enablePlaceOrder() {
        placeOrderButton.setDisable(false);
        placeOrderButton.setStyle("-fx-background-color:#ff6a00;-fx-text-fill:white;-fx-background-radius:12;");
    }

    private void disablePlaceOrder() {
        placeOrderButton.setDisable(true);
        placeOrderButton.setStyle("-fx-background-color:#f0f0f0;-fx-text-fill:#999;-fx-background-radius:12;");
    }

    private void clearCart() {
        currentOrder.getItems().clear();
        cartItemsBox.getChildren().clear(); // 🔥 UI FIX
        recalculate();
    }

    private void placeOrder() {
        currentOrder.setPaymentStrategy(paymentStrategy);
        new OrderDAO().createOrder(1,1,currentOrder.calculateTotal());
        clearCart();
        disablePlaceOrder();
    }
}
