package dao;

import com.example.demo2.db.DBConnection;
import java.sql.*;

public class OrderDAO {

    public int createOrder(
            int customerId,
            int restaurantId,
            double totalPrice
    ) {

        String sql =
                "INSERT INTO orders (customer_id, restaurant_id, total_price) " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, restaurantId);
            stmt.setDouble(3, totalPrice);

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    public void updateOrderStatus(int orderId, String status) {

        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
