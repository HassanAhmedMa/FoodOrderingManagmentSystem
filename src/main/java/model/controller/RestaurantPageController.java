package model.controller;

import com.example.demo2.Navigator;
import dao.MenuItemDAO;
import dao.RestaurantDAO;
import dao.ReviewDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.cart.CartService;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.review.Review;

import java.util.List;

public class RestaurantPageController {

    @FXML private FlowPane menuFlowPane;

    @FXML private Label restaurantNameLabel;
    @FXML private Label restaurantDescLabel;
    @FXML private Label ratingLabel;
    @FXML private Label locationLabel;
    @FXML private Label timeLabel;
    @FXML private Label deliveryLabel;

    @FXML private VBox reviewsBox;
    @FXML private Label reviewsCountLabel;

    @FXML private Label cartBadge;

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    private int restaurantId;

    /* ================= INIT ================= */

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
        loadRestaurantHeader();
        loadMenuItems();
        loadReviews();
        updateCartBadge();
    }

    /* ================= HEADER ================= */

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

    /* ================= MENU ================= */

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

    /* ================= REVIEWS ================= */

    private void loadReviews() {
        List<Review> reviews = reviewDAO.getReviewsForRestaurant(restaurantId);

        reviewsBox.getChildren().clear();
        reviewsCountLabel.setText("Reviews (" + reviews.size() + ")");

        if (reviews.isEmpty()) {
            reviewsBox.getChildren().add(new Label("No reviews yet."));
            return;
        }

        for (Review review : reviews) {
            Label label = new Label(
                    "⭐".repeat(review.getRating()) +
                            "  " + review.getCustomer().getFullName() +
                            "\n" + review.getComment()
            );
            label.getStyleClass().add("review-item");
            reviewsBox.getChildren().add(label);
        }
    }

    /* ================= ADD REVIEW (NEW) ================= */

    @FXML
    private void handleAddReview() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo2/add-review.fxml")
            );

            VBox root = loader.load();

            AddReviewController controller = loader.getController();
            controller.init(restaurantId, this::loadReviews);
            System.out.println("ADD REVIEW BUTTON CLICKED");

            Stage stage = new Stage();
            stage.setTitle("Add Review");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        System.out.println(">>> RestaurantPageController INITIALIZED <<<");
    }

    /* ================= CART ================= */

    private void updateCartBadge() {
        int count = CartService.getInstance().getTotalItemsCount();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisible(count > 0);
    }

    @FXML
    private void handleCart() {
        Navigator.goTo("/com/example/demo2/cart-page.fxml");
    }

    @FXML
    private void handleBack() {
        Navigator.goBack();
    }

    public void goToHome(){
        Navigator.goTo("/com/example/demo2/hello-view.fxml");
    }
}
