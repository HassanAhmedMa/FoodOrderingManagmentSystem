package dao;

import com.example.demo2.db.DBConnection;
import model.restaurant.Restaurant;
import model.user.RestaurantOwner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    /* ================= CREATE ================= */

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

    /* ================= READ ALL ================= */

    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT * FROM restaurants";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                restaurants.add(mapRestaurant(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    /* ================= READ BY ID ================= */

    public Restaurant getRestaurantById(int restaurantId) {

        String sql = "SELECT * FROM restaurants WHERE restaurant_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRestaurant(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* ================= TOP RATED (🔥 FOR HOME PAGE) ================= */

    public List<Restaurant> getTopRatedRestaurants(int limit) {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = """
            SELECT *
            FROM restaurants
            ORDER BY rating_avg DESC
            LIMIT ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                restaurants.add(mapRestaurant(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    /* ================= MAPPER ================= */

    private Restaurant mapRestaurant(ResultSet rs) throws SQLException {

        RestaurantOwner owner =
                new RestaurantOwner(rs.getInt("owner_id"));

        return new Restaurant(
                rs.getInt("restaurant_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getDouble("rating_avg"),
                rs.getBoolean("is_open"),
                rs.getString("image_url"),
                owner
        );
    }

}
