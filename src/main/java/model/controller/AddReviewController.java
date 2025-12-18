package model.controller;

import dao.ReviewDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class AddReviewController {

    @FXML private ToggleButton star1;
    @FXML private ToggleButton star2;
    @FXML private ToggleButton star3;
    @FXML private ToggleButton star4;
    @FXML private ToggleButton star5;

    @FXML private TextArea commentArea;

    private int restaurantId;
    private Runnable onSuccess;

    private final ReviewDAO reviewDAO = new ReviewDAO();

    public void init(int restaurantId, Runnable onSuccess) {
        this.restaurantId = restaurantId;
        this.onSuccess = onSuccess;
    }

    @FXML
    private void handleSubmit() {

        int rating = getSelectedRating();
        String comment = commentArea.getText();

        if (rating == 0 || comment.isBlank()) return;

        int customerId = 1; // 🔴 replace with logged-in user id

        reviewDAO.addReview(customerId, restaurantId, rating, comment);

        onSuccess.run();

        Stage stage = (Stage) commentArea.getScene().getWindow();
        stage.close();
    }

    private int getSelectedRating() {
        ToggleButton[] stars = {star1, star2, star3, star4, star5};
        int count = 0;
        for (ToggleButton star : stars) {
            if (star.isSelected()) count++;
        }
        return count;
    }
}
