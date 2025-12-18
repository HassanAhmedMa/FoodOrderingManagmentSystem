package model.controller;

import dao.ReviewDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AddReviewController {

    @FXML private ToggleButton star1;
    @FXML private ToggleButton star2;
    @FXML private ToggleButton star3;
    @FXML private ToggleButton star4;
    @FXML private ToggleButton star5;

    @FXML private TextArea commentArea;
    @FXML private HBox titleBar;

    private ToggleButton[] stars;

    private double xOffset;
    private double yOffset;

    private int restaurantId;
    private Runnable onSuccess;

    private final ReviewDAO reviewDAO = new ReviewDAO();

    public void init(int restaurantId, Runnable onSuccess) {
        this.restaurantId = restaurantId;
        this.onSuccess = onSuccess;
    }

    @FXML
    private void initialize() {

        // ⭐ STAR LOGIC
        stars = new ToggleButton[]{star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnAction(e -> selectStars(rating));
        }

        // 🖱️ WINDOW DRAG
        titleBar.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        titleBar.setOnMouseDragged(e -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    private void selectStars(int rating) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setSelected(i < rating);
        }
    }

    private int getSelectedRating() {
        int count = 0;
        for (ToggleButton star : stars) {
            if (star.isSelected()) count++;
        }
        return count;
    }

    @FXML
    private void handleSubmit() {
        int rating = getSelectedRating();
        String comment = commentArea.getText();

        if (rating == 0 || comment.isBlank()) return;

        int customerId = 1; // TODO replace with logged-in user

        reviewDAO.addReview(customerId, restaurantId, rating, comment);

        onSuccess.run();
        handleClose();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) commentArea.getScene().getWindow();
        stage.close();
    }
}
