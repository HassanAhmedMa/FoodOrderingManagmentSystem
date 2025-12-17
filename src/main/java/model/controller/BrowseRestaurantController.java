package model.controller;

import com.example.demo2.Navigator;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseRestaurantController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;
    @FXML private Label resultCountLabel;
    @FXML private FlowPane restaurantContainer;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private List<Restaurant> allRestaurants;

    /* ================= INIT ================= */

    @FXML
    public void initialize() {

        sortCombo.getItems().addAll(
                "Top Rated",
                "Name (A-Z)",
                "Name (Z-A)"
        );
        sortCombo.getSelectionModel().selectFirst();

        loadRestaurants();

        searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        sortCombo.valueProperty().addListener((obs, o, n) -> applyFilters());
    }

    /* ================= LOAD ================= */

    private void loadRestaurants() {
        allRestaurants = restaurantDAO.getAllRestaurants();
        applyFilters();
    }

    /* ================= FILTER ================= */

    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase();

        List<Restaurant> filtered = allRestaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(search))
                .collect(Collectors.toList());

        switch (sortCombo.getValue()) {
            case "Name (A-Z)" ->
                    filtered.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            case "Name (Z-A)" ->
                    filtered.sort((a, b) -> b.getName().compareToIgnoreCase(a.getName()));
            default ->
                    filtered.sort((a, b) -> Double.compare(b.getRatingAvg(), a.getRatingAvg()));
        }

        displayRestaurants(filtered);
    }

    /* ================= UI ================= */

    private void displayRestaurants(List<Restaurant> restaurants) {

        restaurantContainer.getChildren().clear();

        for (Restaurant r : restaurants) {
            restaurantContainer.getChildren().add(createRestaurantCard(r));
        }

        resultCountLabel.setText(
                "Showing " + restaurants.size() + " restaurants"
        );
    }

    /* ================= CARD ================= */

    private VBox createRestaurantCard(Restaurant r) {

        ImageView image = new ImageView();
        image.setFitWidth(300);
        image.setFitHeight(160);
        image.setPreserveRatio(false);
        image.setSmooth(true);
        image.setMouseTransparent(true);

        loadImageSafely(image, r.getImageUrl());

        Label rating = new Label("⭐ " + r.getRatingAvg());
        rating.getStyleClass().add("rating-badge");

        StackPane imageWrapper = new StackPane(image);
        StackPane.setAlignment(rating, Pos.TOP_RIGHT);
        StackPane.setMargin(rating, new Insets(10));
        imageWrapper.getChildren().add(rating);

        if (!r.isOpen()) {
            Label closed = new Label("Currently Closed");
            closed.setStyle("""
                -fx-background-color: rgba(0,0,0,0.6);
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 6 12;
                -fx-background-radius: 20;
            """);
            StackPane.setAlignment(closed, Pos.CENTER);
            imageWrapper.getChildren().add(closed);
        }

        Label name = new Label(r.getName());
        name.getStyleClass().add("restaurant-name");

        Label desc = new Label(r.getDescription());
        desc.getStyleClass().add("restaurant-desc");
        desc.setWrapText(true);

        Label info = new Label("25–35 min · " + r.getLocation());
        info.getStyleClass().add("restaurant-info");

        VBox card = new VBox(10, imageWrapper, name, desc, info);
        card.getStyleClass().add("restaurant-card");

        card.setOnMousePressed(e ->
                card.setStyle(card.getStyle() + "; -fx-opacity: 0.9;")
        );

        card.setOnMouseReleased(e ->
                card.setStyle(card.getStyle().replace("-fx-opacity: 0.9;", ""))
        );

        // ✅ CORRECT NAVIGATION
        card.setOnMouseClicked(e ->
                Navigator.goToRestaurant(r.getId())
        );

        return card;
    }

    /* ================= IMAGE SAFE LOADER ================= */

    private void loadImageSafely(ImageView imageView, String path) {
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

    /* ================= BACK ================= */

    @FXML
    private void handleBack() {
        Navigator.goBack(); // ✅ FIXED
    }
}
