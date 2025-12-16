package model.controller;

import com.example.demo2.Navigator;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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

    private VBox createRestaurantCard(Restaurant r) {

        ImageView image = new ImageView();
        image.setFitWidth(300);
        image.setFitHeight(160);
        image.setPreserveRatio(false);
        image.setSmooth(true);
        image.setMouseTransparent(true); // 🔥 click fix

        loadImageSafely(image, r.getImageUrl());

        Label name = new Label(r.getName());
        name.setStyle("-fx-font-weight: bold;");

        Label desc = new Label(r.getDescription());
        desc.setStyle("-fx-text-fill: #777;");

        Label rating = new Label("⭐ " + r.getRatingAvg());

        Label info = new Label(
                "25-35 min · " + r.getLocation() + " · $2.99 delivery"
        );
        info.setStyle("-fx-text-fill: #ff6a00;");

        VBox card = new VBox(10, image, name, desc, rating, info);
        card.setPrefWidth(300);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10,0,0,4);
        """);

        card.setOnMouseClicked(e ->
                Navigator.goToRestaurant(r.getId())
        );

        return card;
    }

    /* ================= IMAGE SAFE LOADER ================= */

    private void loadImageSafely(ImageView imageView, String path) {

        if (path == null || path.isBlank()) {
            return;
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
    private void handleBack() {
        Navigator. goTo("/com/example/demo2/hello-view.fxml");
    }
}
