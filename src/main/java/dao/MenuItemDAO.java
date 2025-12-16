package dao;

import com.example.demo2.db.DBConnection;
import model.restaurant.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    /* ================= INSERT ================= */

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= READ ================= */

    public List<MenuItem> getMenuItemsByRestaurant(int restaurantId) {

        List<MenuItem> items = new ArrayList<>();

        String sql = """
            SELECT item_id, restaurant_id, name, description, price,
                   category, image_url, available
            FROM menu_items
            WHERE restaurant_id = ? AND available = true
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                MenuItem item = new MenuItem(
                        rs.getInt("item_id"),          // ✅ item_id
                        rs.getInt("restaurant_id"),    // ✅ restaurant_id
                        rs.getString("name"),
                        rs.getDouble("price")
                );


                item.setDescription(rs.getString("description"));
                item.setCategory(rs.getString("category"));
                item.setImageUrl(rs.getString("image_url"));
                item.setAvailable(rs.getBoolean("available"));

                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
    public void createMenuItem(MenuItem item) {

        String sql = """
        INSERT INTO menu_items
        (restaurant_id, name, description, price, category, image_url, available)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getRestaurantId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getCategory());
            stmt.setString(6, item.getImageUrl());
            stmt.setBoolean(7, item.isAvailable());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE ================= */

    public void deleteMenuItem(int itemId) {

        String sql = "UPDATE menu_items SET available = false WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= UPDATE ================= */

    public void updateMenuItem(MenuItem item) {

        String sql = """
        UPDATE menu_items
        SET name = ?,
            description = ?,
            price = ?,
            category = ?,
            image_url = ?,
            available = ?
        WHERE item_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getCategory());
            stmt.setString(5, item.getImageUrl());
            stmt.setBoolean(6, item.isAvailable());
            stmt.setInt(7, item.getItemId()); // 🔑 THIS IS WHY ID MATTERS

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
