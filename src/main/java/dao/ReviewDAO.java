package dao;

import com.example.demo2.db.DBConnection;

import java.sql.*;

public class ReviewDAO {

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

    public ResultSet getReviewsForRestaurant(int restaurantId) {
        String sql = "SELECT * FROM reviews WHERE restaurant_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, restaurantId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
