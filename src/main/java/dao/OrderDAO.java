package dao;

import com.example.demo2.db.DBConnection;
import model.order.Order;
import model.order.OrderItem;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.state.DeliveredState;
import model.state.OrderState;
import model.state.PlacedState;
import model.state.PreparingState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<Order> getOrdersByCustomer(int customerId) {

        List<Order> orders = new ArrayList<>();

        String orderSql = """
        SELECT 
            o.order_id,
            o.status,
            r.restaurant_id,
            r.name AS restaurant_name
        FROM orders o
        JOIN restaurants r ON o.restaurant_id = r.restaurant_id
        WHERE o.customer_id = ?
        ORDER BY o.order_id DESC
    """;

        String itemsSql = """
    SELECT
        oi.quantity,
        m.restaurant_id,
        m.name,
        m.price
    FROM order_items oi
    JOIN menu_items m ON oi.item_id = m.item_id
    WHERE oi.order_id = ?
""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {

            orderStmt.setInt(1, customerId);
            ResultSet orderRs = orderStmt.executeQuery();

            while (orderRs.next()) {

                // 1️⃣ Restaurant
                Restaurant restaurant = new Restaurant(
                        orderRs.getInt("restaurant_id"),
                        orderRs.getString("restaurant_name"),
                        null, null, 0, true, null, null
                );

                // 2️⃣ Order
                Order order = new Order(
                        orderRs.getInt("order_id"),
                        null, // customer already known
                        restaurant
                );

                // 3️⃣ State (your rules)
                order.setState(mapState(orderRs.getString("status")));

                // 4️⃣ Load items for THIS order
                try (PreparedStatement itemStmt = conn.prepareStatement(itemsSql)) {

                    itemStmt.setInt(1, order.getId());
                    ResultSet itemRs = itemStmt.executeQuery();

                    while (itemRs.next()) {

                        MenuItem menuItem = new MenuItem(
                                itemRs.getInt("restaurant_id"),
                                itemRs.getString("name"),
                                itemRs.getDouble("price")
                        );

                        OrderItem orderItem = new OrderItem(
                                menuItem,
                                itemRs.getInt("quantity")
                        );

                        order.addItem(orderItem);
                    }
                }

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }



    private OrderState mapState(String status) {
        return switch (status) {
            case "CONFIRMED" -> new PlacedState();
            case "PREPARING" -> new PreparingState();
            case "DELIVERED" -> new DeliveredState();
            default -> new PlacedState();
        };
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
