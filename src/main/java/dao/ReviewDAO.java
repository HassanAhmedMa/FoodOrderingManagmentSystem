package dao;

import com.example.demo2.db.DBConnection;
import model.review.Review;
import model.restaurant.Restaurant;
import model.user.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    /* ================= ADD REVIEW ================= */

    public void addReview(int customerId, int restaurantId, int rating, String comment) {

        String sql = """
            INSERT INTO reviews (customer_id, restaurant_id, rating, comment)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, restaurantId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= READ REVIEWS ================= */

    public List<Review> getReviewsForRestaurant(int restaurantId) {

        List<Review> reviews = new ArrayList<>();

        String sql = """
            SELECT r.review_id, r.rating, r.comment,
                   u.user_id, u.full_name
            FROM reviews r
            JOIN users u ON r.customer_id = u.user_id
            WHERE r.restaurant_id = ?
            ORDER BY r.created_at DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Customer customer = new Customer(
                        rs.getInt("user_id"),
                        rs.getString("full_name")
                );

                Review review = new Review(
                        rs.getInt("review_id"),
                        customer,
                        new Restaurant(restaurantId),
                        rs.getInt("rating"),
                        rs.getString("comment")
                );

                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
