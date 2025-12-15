package dao;

import com.example.demo2.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MenuItemDAO {

    public void addMenuItem(
            int restaurantId,
            String name,
            String description,
            double price,
            String category,
            String imageUrl,
            boolean available
    ) {

        String sql = """
            INSERT INTO menu_items
            (restaurant_id, name, description, price, category, image_url, available)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setDouble(4, price);
            stmt.setString(5, category);
            stmt.setString(6, imageUrl);
            stmt.setBoolean(7, available);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
