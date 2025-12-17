package dao;

import com.example.demo2.db.DBConnection;
import model.order.Order;
import model.order.OrderItem;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;
import model.state.*;
import model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // ======================================================
    // CREATE ORDER
    // ======================================================
    public int createOrder(int customerId, int restaurantId, double totalPrice) {

        String sql = """
            INSERT INTO orders (customer_id, restaurant_id, total_price, status)
            VALUES (?, ?, ?, 'PLACED')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, restaurantId);
            stmt.setDouble(3, totalPrice);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ======================================================
    // STATUS UPDATE WITH ROLE CHECK
    // ======================================================
    public boolean updateOrderStatus(int orderId, String nextStatus, String role) {

        String currentStatus = getCurrentStatus(orderId);
        if (currentStatus == null) return false;

        if (!isAllowed(currentStatus, nextStatus, role)) {
            System.out.println("❌ Status change blocked: "
                    + currentStatus + " → " + nextStatus);
            return false;
        }

        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nextStatus);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // AUTO ASSIGN DELIVERY (MAX 3 ORDERS)
    // ======================================================
    public boolean assignOrderAutomatically(int orderId) {

        UserDAO userDAO = new UserDAO();
        User delivery = userDAO.getFirstAvailableDeliveryStaffWithCapacity();

        if (delivery == null) {
            System.out.println("❌ No delivery staff available");
            return false;
        }

        String sql = """
            UPDATE orders
            SET delivery_id = ?, status = 'OUT_FOR_DELIVERY'
            WHERE order_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, delivery.getId());
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // DELIVERY MARK AS DELIVERED
    // ======================================================
    public void markOrderDelivered(int orderId) {

        String sql = "UPDATE orders SET status = 'DELIVERED' WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // GET ORDERS BY RESTAURANT
    // ======================================================
    public List<Order> getOrdersByRestaurant(int restaurantId) {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.status,
                   r.restaurant_id, r.name AS restaurant_name
            FROM orders o
            JOIN restaurants r ON o.restaurant_id = r.restaurant_id
            WHERE o.restaurant_id = ?
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Restaurant restaurant = new Restaurant(
                        rs.getInt("restaurant_id"),
                        rs.getString("restaurant_name"),
                        null, null, 0, true, null, null
                );

                Order order = new Order(
                        rs.getInt("order_id"),
                        null,
                        restaurant
                );

                order.setState(mapState(rs.getString("status")));
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // ======================================================
    // GET ORDERS BY DELIVERY STAFF
    // ======================================================
    public List<Order> getOrdersByDeliveryStaff(int deliveryId) {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.status,
                   r.restaurant_id, r.name AS restaurant_name
            FROM orders o
            JOIN restaurants r ON o.restaurant_id = r.restaurant_id
            WHERE o.delivery_id = ?
              AND o.status = 'OUT_FOR_DELIVERY'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Restaurant restaurant = new Restaurant(
                        rs.getInt("restaurant_id"),
                        rs.getString("restaurant_name"),
                        null, null, 0, true, null, null
                );

                Order order = new Order(
                        rs.getInt("order_id"),
                        null,
                        restaurant
                );

                order.setState(mapState(rs.getString("status")));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // ======================================================
    // HELPERS
    // ======================================================
    private String getCurrentStatus(int orderId) {

        String sql = "SELECT status FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("status");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAllowed(String current, String next, String role) {

        if ("CUSTOMER".equals(role)) return false;

        if ("RESTAURANT".equals(role)) {
            return switch (current) {
                case "PLACED" -> "CONFIRMED".equals(next);
                case "CONFIRMED" -> "PREPARING".equals(next);
                case "PREPARING" -> "OUT_FOR_DELIVERY".equals(next);
                default -> false;
            };
        }

        if ("DELIVERY".equals(role)) {
            return "OUT_FOR_DELIVERY".equals(current)
                    && "DELIVERED".equals(next);
        }

        return false;
    }

    private OrderState mapState(String status) {
        return switch (status) {
            case "PLACED" -> new PlacedState();
            case "CONFIRMED" -> new PreparingState(); // visual only
            case "PREPARING" -> new PreparingState();
            case "OUT_FOR_DELIVERY" -> new OutForDeliveryState();
            case "DELIVERED" -> new DeliveredState();
            default -> new PlacedState();
        };
    }

    // ======================================================
    // STATS
    // ======================================================
    public int getTotalOrdersByRestaurant(int restaurantId) {

        String sql = "SELECT COUNT(*) FROM orders WHERE restaurant_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

}
