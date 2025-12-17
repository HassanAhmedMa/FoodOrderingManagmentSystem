package model.controller;

import com.example.demo2.Navigator;
import dao.OrderDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.cart.CartService;
import model.order.OrderItem;
import model.payment.*;

public class CartController {

    @FXML private VBox orderSummary;
    @FXML private VBox cartItemsBox;
    @FXML private Button clearCartButton;
    @FXML private Button backButton;

    private TextField addressField;
    private Button cardButton, cashButton, placeOrderButton;
    private Label subtotalLabel, taxLabel, totalLabel;

    private PaymentStrategy paymentStrategy;

    private static final double TAX_RATE = 0.10;
    private static final double DELIVERY_FEE = 2.99;

    /* ================= INIT ================= */

    @FXML
    public void initialize() {

        addressField = (TextField) orderSummary.lookup("#addressField");
        cardButton = (Button) orderSummary.lookup("#cardButton");
        cashButton = (Button) orderSummary.lookup("#cashButton");
        placeOrderButton = (Button) orderSummary.lookup("#placeOrderButton");
        subtotalLabel = (Label) orderSummary.lookup("#subtotalLabel");
        taxLabel = (Label) orderSummary.lookup("#taxLabel");
        totalLabel = (Label) orderSummary.lookup("#totalLabel");

        cardButton.setOnAction(e -> selectCard());
        cashButton.setOnAction(e -> selectCash());
        clearCartButton.setOnAction(e -> clearCart());
        placeOrderButton.setOnAction(e -> placeOrder());

        addressField.textProperty().addListener((a, b, c) -> validate());

        loadCart();
    }

    /* ================= BACK ================= */

    @FXML
    private void handleBack() {
        Navigator.goBack();
    }

    /* ================= LOAD CART ================= */

    private void loadCart() {
        cartItemsBox.getChildren().clear();

        for (OrderItem item : CartService.getInstance().getItems()) {
            addCartItem(item);
        }

        recalculate();
        validate();
    }

    /* ================= PAYMENT ================= */

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

    /* ================= CALCULATION ================= */

    public void recalculate() {

        double subtotal = CartService.getInstance().getItems().stream()
                .mapToDouble(i -> i.getItem().getPrice() * i.getQuantity())
                .sum();

        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax + DELIVERY_FEE;

        subtotalLabel.setText(String.format("Subtotal %.2f$", subtotal));
        taxLabel.setText(String.format("Tax %.2f$", tax));
        totalLabel.setText(String.format("Total %.2f$", total));
    }

    /* ================= VALIDATION ================= */

    private void validate() {
        boolean valid =
                !CartService.getInstance().getItems().isEmpty()
                        && paymentStrategy != null
                        && !addressField.getText().isBlank();

        placeOrderButton.setDisable(!valid);
    }

    /* ================= ACTIONS ================= */

    private void clearCart() {
        CartService.getInstance().clear();
        cartItemsBox.getChildren().clear();
        recalculate();
        validate();
    }

    private void placeOrder() {

        double total = CartService.getInstance().getItems().stream()
                .mapToDouble(i -> i.getItem().getPrice() * i.getQuantity())
                .sum();

        new OrderDAO().createOrder(
                1, // customerId
                1, // restaurantId
                total
        );

        clearCart();
        paymentStrategy = null;
        validate();
    }

    /* ================= ITEM UI ================= */

    public void removeItem(OrderItem item, HBox cardRoot) {
        CartService.getInstance().getItems().remove(item);
        cartItemsBox.getChildren().remove(cardRoot);
        recalculate();
        validate();
    }

    private void addCartItem(OrderItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo2/cart-item-card.fxml")
            );

            HBox card = loader.load();

            CartItemCardController controller = loader.getController();
            controller.init(item, this, card);

            cartItemsBox.getChildren().add(card);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
