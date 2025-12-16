package com.example.demo2;

import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.restaurant.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class BrowseRestaurantController {

    // ===== FXML FIELDS (must match fx:id exactly) =====

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> sortCombo;

    @FXML
    private CheckBox openNowCheck;

    @FXML
    private Label resultCountLabel;

    @FXML
    private FlowPane restaurantContainer;

    // ===== DAO =====
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    // Keep all restaurants in memory
    private List<Restaurant> allRestaurants;

    // ===== INITIALIZE (called automatically) =====
    @FXML
    public void initialize() {
        System.out.println("BrowseRestaurantController loaded");
        // Setup sort options
        sortCombo.getItems().addAll(
                "Top Rated",
                "Name (A-Z)",
                "Name (Z-A)"
        );
        sortCombo.getSelectionModel().selectFirst();

        // Load data from DB
        loadRestaurants();

        // Listeners for live filtering
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        openNowCheck.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    // ===== LOAD RESTAURANTS FROM DB =====
    private void loadRestaurants() {
        allRestaurants = restaurantDAO.getAllRestaurants();
        applyFilters();
    }

    // ===== FILTER + SORT =====
    private void applyFilters() {

        String searchText = searchField.getText().toLowerCase();

        List<Restaurant> filtered = allRestaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(searchText))
                .filter(r -> !openNowCheck.isSelected() || r.isOpen())
                .collect(Collectors.toList());

        switch (sortCombo.getValue()) {
            case "Name (A-Z)" ->
                    filtered.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            case "Name (Z-A)" ->
                    filtered.sort((a, b) -> b.getName().compareToIgnoreCase(a.getName()));
            default ->
                    filtered.sort((a, b) ->
                            Double.compare(b.getRatingAvg(), a.getRatingAvg()));
        }

        displayRestaurants(filtered);
    }

    // ===== DISPLAY RESTAURANTS =====
    private void displayRestaurants(List<Restaurant> restaurants) {

        restaurantContainer.getChildren().clear();

        for (Restaurant r : restaurants) {
            restaurantContainer.getChildren().add(createRestaurantCard(r));
        }

        resultCountLabel.setText(
                "Showing " + restaurants.size() + " restaurants"
        );
    }

    // ===== SINGLE RESTAURANT CARD =====
    private VBox createRestaurantCard(Restaurant r) {

        Label name = new Label(r.getName());
        name.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label rating = new Label("⭐ " + r.getRatingAvg());
        Label location = new Label(r.getLocation());

        Label status = new Label(r.isOpen() ? "Open" : "Closed");
        status.setStyle(r.isOpen()
                ? "-fx-text-fill: green;"
                : "-fx-text-fill: red;"
        );

        VBox card = new VBox(8, name, rating, location, status);
        card.setPrefWidth(260);
        card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 15;
            -fx-background-radius: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
        """);

        // Click event (later: open restaurant details)
        card.setOnMouseClicked(e ->
                System.out.println("Clicked restaurant ID: " + r.getId())
        );

        return card;
    }
}