package com.example.demo2.db;

import dao.*;

import java.sql.ResultSet;

public class DAOTestRunner {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        OrderDAO orderDAO = new OrderDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ReviewDAO reviewDAO = new ReviewDAO();

        System.out.println("===== DAO TEST START =====");

        /* ---------------- USERS ---------------- */
        userDAO.createUser(
                "Test Customer",
                "test@foodhub.com",
                "hashed123",
                "CUSTOMER",
                "01012345678"
        );
        System.out.println("✔ User inserted");

        /* ---------------- RESTAURANT ---------------- */
        restaurantDAO.createRestaurant(
                2, // owner_id (Pizza Owner from seed data)
                "DAO Test Restaurant",
                "Testing DAO logic",
                "Downtown"
        );
        System.out.println("✔ Restaurant inserted");

        /* ---------------- MENU ITEM ---------------- */
        menuItemDAO.addMenuItem(
                1,               // restaurant_id
                "DAO Burger",
                "Burger for DAO test",
                99.99,
                "Fast Food",
                null,
                true              // ✅ AVAILABLE (THIS WAS MISSING)
        );
        System.out.println("✔ Menu item inserted");

        /* ---------------- ORDER ---------------- */
        int orderId = orderDAO.createOrder(
                1,      // customer_id
                1,      // restaurant_id
                99.99   // total_price
        );
        System.out.println("✔ Order created with ID = " + orderId);

        /* ---------------- PAYMENT ---------------- */
        paymentDAO.createPayment(
                orderId,
                "CARD",
                "PAID"
        );
        System.out.println("✔ Payment recorded");

        /* ---------------- REVIEW ---------------- */
        reviewDAO.addReview(
                1,      // customer_id
                1,      // restaurant_id
                5,
                "DAO test review – excellent!"
        );
        System.out.println("✔ Review added");

        /* ---------------- READ TEST ---------------- */
        try {
            ResultSet rs = restaurantDAO.getAllRestaurants();

            System.out.println("\n--- Restaurants in DB ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("restaurant_id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("location")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n===== DAO TEST FINISHED =====");
    }
}
