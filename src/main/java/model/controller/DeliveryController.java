package model.controller;

import dao.OrderDAO;
import model.order.Order;

public class DeliveryController {

    private final OrderDAO orderDAO = new OrderDAO();

    public void deliverOrder(Order order) {

        System.out.println("Delivering order #" + order.getId());

        // ✅ update DB
        orderDAO.updateOrderStatus(order.getId(), "DELIVERED");

        // ✅ update in-memory object
        order.setStatus("DELIVERED");
    }
}
