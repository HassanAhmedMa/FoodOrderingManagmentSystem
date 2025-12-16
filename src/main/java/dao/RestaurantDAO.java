package dao;

import com.example.demo2.db.DBConnection;
import model.restaurant.Restaurant;
import model.user.RestaurantOwner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    // ================= CREATE =================
    public void createRestaurant(Restaurant restaurant) {

        String sql = """
                INSERT INTO restaurants
                (owner_id, name, description, location, rating_avg, is_open)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurant.getOwner().getId());
            stmt.setString(2, restaurant.getName());
            stmt.setString(3, restaurant.getDescription());
            stmt.setString(4, restaurant.getLocation());
            stmt.setDouble(5, restaurant.getRatingAvg());
            stmt.setBoolean(6, restaurant.isOpen());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= READ (THIS ONE 👇) =================
    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT * FROM restaurants";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                RestaurantOwner owner =
                        new RestaurantOwner(rs.getInt("owner_id"));

                Restaurant restaurant = new Restaurant(
                        rs.getInt("restaurant_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getDouble("rating_avg"),
                        rs.getBoolean("is_open"),
                        owner
                );

                restaurants.add(restaurant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}