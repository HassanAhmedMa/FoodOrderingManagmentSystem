package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.cart.CartService;
import model.restaurant.Restaurant;

import java.net.URL;
import java.util.List;

public class HomePageController {

    // 🛒 CART BADGE
    @FXML private Label cartBadge;
    public Label loggedInUser;
    @FXML
    private HBox popularRestaurantsBox;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    /* ================= INIT ================= */

    @FXML
    public void initialize() {

        loadPopularRestaurants();
        updateCartBadge();
    }

    /* ================= NAV ================= */

    @FXML
    private void onBrowseClicked() {
        Navigator.goTo("/com/example/demo2/browse-restaurants.fxml");
    }

    /* ================= DATA ================= */

    private void loadPopularRestaurants() {
        loggedInUser.setText(Session.getUser().getFullName());
        List<Restaurant> restaurants =
                restaurantDAO.getTopRatedRestaurants(4);

        popularRestaurantsBox.getChildren().clear();

        for (Restaurant r : restaurants) {
            popularRestaurantsBox.getChildren().add(createRestaurantCard(r));
        }
    }

    /* ================= UI CARD ================= */

    private VBox createRestaurantCard(Restaurant r) {

        // ---------- Image ----------
        ImageView image = new ImageView();
        image.setFitWidth(270);
        image.setFitHeight(160);
        image.setPreserveRatio(false);
        image.setSmooth(true);
        image.setMouseTransparent(true);

        loadImageSafely(image, r.getImageUrl());

        // Image container to allow badge overlay
        StackPane imageWrap = new StackPane(image);
        imageWrap.setStyle("""
        -fx-background-radius: 12;
        -fx-background-color: #f3f4f6;
    """);

        // ---------- Rating badge (top-right on image) ----------
        Label badge = new Label("★ " + r.getRatingAvg());
        badge.setStyle("""
        -fx-background-color: rgba(0,0,0,0.75);
        -fx-text-fill: white;
        -fx-padding: 4 10;
        -fx-background-radius: 999;
        -fx-font-size: 12px;
        -fx-font-weight: bold;
    """);
        StackPane.setAlignment(badge, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(badge, new Insets(8, 8, 0, 0));
        imageWrap.getChildren().add(badge);

        // ---------- Text ----------
        Label name = new Label(r.getName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #111827;");

        Label desc = new Label(r.getDescription());
        desc.setStyle("-fx-text-fill: #777;");

        Label info = new Label("25-35 min · " + r.getLocation() + " · $2.99 delivery");
        info.setStyle("-fx-text-fill: #ff6a00; -fx-font-size: 12px;");

        // ---------- Card ----------
        VBox card = new VBox(10, imageWrap, name, desc, info);
        card.setPrefWidth(300);
        card.setPadding(new Insets(12));
        card.setStyle("""
        -fx-background-color: white;
        -fx-background-radius: 16;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
    """);

        // Hover effect (same vibe as browse cards)
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.05);
            card.setScaleY(1.05);
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #ff6a00;");
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #111827;");
        });

        // Keep your exact same logic (click opens restaurant page)
        card.setOnMouseClicked(e -> Navigator.goToRestaurant(r.getId()));

        return card;
    }


    /* ================= IMAGE SAFE LOADER ================= */

    private void loadImageSafely(ImageView imageView, String path) {

        if (path == null || path.isBlank()) {
            return; // no image → empty card (allowed)
        }

        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                imageView.setImage(new Image(url.toExternalForm(), true));
            } else {
                System.out.println("Image not found: " + path);
            }
        } catch (Exception e) {
            System.out.println("Failed to load image: " + path);
        }
    }
    @FXML
    public void goToOrders(){
        Navigator.goTo("/com/example/demo2/my-orders.fxml");
    }
    public void goToHome(){
        Navigator.goTo("/com/example/demo2/hello-view.fxml");
    }

    @FXML
    private void handleCart() {
        Navigator.goTo("/com/example/demo2/cart-page.fxml");
    }

    @FXML
    private void updateCartBadge() {
        int count = CartService.getInstance().getTotalItemsCount();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisible(count > 0);
    }
}
