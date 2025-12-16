package model.controller;

import com.example.demo2.Navigator;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.restaurant.Restaurant;

import java.net.URL;
import java.util.List;

public class HomePageController {

    @FXML
    private HBox popularRestaurantsBox;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @FXML
    public void initialize() {
        loadPopularRestaurants();
    }

    @FXML
    private void onBrowseClicked() {
        Navigator.goTo("/com/example/demo2/browse-restaurants.fxml");
    }

    private void loadPopularRestaurants() {

        List<Restaurant> restaurants =
                restaurantDAO.getTopRatedRestaurants(4);

        popularRestaurantsBox.getChildren().clear();

        for (Restaurant r : restaurants) {
            popularRestaurantsBox.getChildren().add(createRestaurantCard(r));
        }
    }

    private VBox createRestaurantCard(Restaurant r) {

        ImageView image = new ImageView();
        image.setFitWidth(300);
        image.setFitHeight(150);
        image.setPreserveRatio(false);
        image.setSmooth(true);
        image.setMouseTransparent(true);

        image.setImage(loadImage(r.getImageUrl()));

        Label name = new Label(r.getName());
        name.setStyle("-fx-font-weight: bold;");

        Label desc = new Label(r.getDescription());
        desc.setStyle("-fx-text-fill: #777;");

        Label rating = new Label(" " + r.getRatingAvg());

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

        // 🔥 CLICK WORKS FOR ALL CARDS
        card.setOnMouseClicked(e ->
                Navigator.goToRestaurant(r.getId())
        );

        return card;
    }

    /* ================= IMAGE LOADING ================= */

    private Image loadImage(String path) {

        if (path == null || path.isBlank()) {
            return loadPlaceholder();
        }

        try {
            URL url = getClass().getResource(path);

            if (url == null) {
                System.out.println(" Image not found: " + path);
                return loadPlaceholder();
            }

            return new Image(url.toExternalForm(), true);

        } catch (Exception e) {
            return loadPlaceholder();
        }
    }


    private Image loadPlaceholder() {

        URL url = getClass().getResource("/images/restaurants/burgerhub.jpg");

        if (url == null) {
            System.out.println("Placeholder image missing!");
            // absolute fallback (never crashes)
            return new Image("https://via.placeholder.com/300x150.png");
        }

        return new Image(url.toExternalForm(), true);
    }

}
