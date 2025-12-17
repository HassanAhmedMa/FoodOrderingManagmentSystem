package dao;

import com.example.demo2.db.DBConnection;
import model.restaurant.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    /* ================= CREATE ================= */

    public void addMenuItem(MenuItem item) {

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
            stmt.setBoolean(7, true); // always available on insert

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= READ (DEBUG) ================= */

    public List<MenuItem> getMenuItemsByRestaurant(int restaurantId) {

        List<MenuItem> items = new ArrayList<>();

        String sql = """
            SELECT item_id, restaurant_id, name, description, price,
                   category, image_url, available
            FROM menu_items
            WHERE restaurant_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("========================================");
            System.out.println("MenuItemDAO → DB           = " + conn.getCatalog());
            System.out.println("MenuItemDAO → restaurantId = " + restaurantId);
            System.out.println("========================================");

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                System.out.println(
                        "FOUND MENU ITEM → id=" + rs.getInt("item_id") +
                                ", restaurant_id=" + rs.getInt("restaurant_id") +
                                ", name=" + rs.getString("name") +
                                ", available=" + rs.getBoolean("available")
                );

                MenuItem item = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("restaurant_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );

                item.setDescription(rs.getString("description"));
                item.setCategory(rs.getString("category"));
                item.setImageUrl(rs.getString("image_url"));
                item.setAvailable(rs.getBoolean("available"));

                items.add(item);
            }

            System.out.println("TOTAL MENU ITEMS RETURNED = " + count);
            System.out.println("========================================");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
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
            stmt.setInt(7, item.getItemId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE (SOFT) ================= */

    public void deleteMenuItem(int itemId) {

        String sql = """
            UPDATE menu_items
            SET available = 0
            WHERE item_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
