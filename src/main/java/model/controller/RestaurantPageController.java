package model.controller;

import com.example.demo2.Navigator;
import dao.MenuItemDAO;
import dao.RestaurantDAO;
import dao.ReviewDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.review.Review;

import java.util.List;

public class RestaurantPageController {

    @FXML private FlowPane menuFlowPane;
    @FXML private Button backButton;

    @FXML private Label restaurantNameLabel;
    @FXML private Label restaurantDescLabel;
    @FXML private Label ratingLabel;
    @FXML private Label locationLabel;
    @FXML private Label timeLabel;
    @FXML private Label deliveryLabel;

    @FXML private VBox reviewsBox;
    @FXML private Label reviewsCountLabel;

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    private int restaurantId;

    // 🔥 MUST BE CALLED FROM NAVIGATION
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
        loadRestaurantHeader();
        loadMenuItems();
        loadReviews();
    }

    private void loadRestaurantHeader() {
        Restaurant r = restaurantDAO.getRestaurantById(restaurantId);
        if (r == null) return;

        restaurantNameLabel.setText(r.getName());
        restaurantDescLabel.setText(r.getDescription());
        ratingLabel.setText("⭐ " + r.getRatingAvg());
        locationLabel.setText("📍 " + r.getLocation());
        timeLabel.setText("30-40 min");
        deliveryLabel.setText("$1.99 delivery");
    }

    private void loadMenuItems() {
        List<MenuItem> items = menuItemDAO.getMenuItemsByRestaurant(restaurantId);
        menuFlowPane.getChildren().clear();

        for (MenuItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/demo2/menu-item-card.fxml")
                );

                HBox card = loader.load();

                MenuItemCardController controller = loader.getController();
                controller.setData(item);

                menuFlowPane.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadReviews() {
        List<Review> reviews = reviewDAO.getReviewsForRestaurant(restaurantId);

        reviewsBox.getChildren().clear();
        reviewsCountLabel.setText("Reviews (" + reviews.size() + ")");

        if (reviews.isEmpty()) {
            Label empty = new Label("No reviews yet. Be the first to review!");
            empty.getStyleClass().add("review-text");
            reviewsBox.getChildren().add(empty);
            return;
        }

        for (Review review : reviews) {
            reviewsBox.getChildren().add(createReviewCard(review));
        }
    }

    private VBox createReviewCard(Review review) {
        Label user = new Label(review.getCustomer().getName());
        user.getStyleClass().add("review-user");

        Label rating = new Label("⭐".repeat(review.getRating()));
        rating.getStyleClass().add("review-rating");

        Label comment = new Label(review.getComment());
        comment.setWrapText(true);
        comment.getStyleClass().add("review-comment");

        VBox card = new VBox(5, user, rating, comment);
        card.getStyleClass().add("review-card");

        return card;
    }

    @FXML
    private void handleBack() {
        Navigator.goBack();
    }
}
