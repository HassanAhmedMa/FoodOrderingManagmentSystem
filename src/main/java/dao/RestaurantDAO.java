package dao;

import com.example.demo2.db.DBConnection;

import java.sql.*;

public class RestaurantDAO {

    public void createRestaurant(int ownerId, String name, String description, String location) {
        String sql = """
                INSERT INTO restaurants (owner_id, name, description, location)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllRestaurants() {
        String sql = "SELECT * FROM restaurants";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
