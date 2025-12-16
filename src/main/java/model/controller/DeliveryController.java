package model.controller;

import dao.OrderDAO;
import model.order.Order;
import model.state.DeliveredState;

public class DeliveryController {

    private final OrderDAO orderDAO = new OrderDAO();

    public void deliverOrder(Order order) {

        System.out.println("Delivering order #" + order.getId());

        // ✅ update DB
        orderDAO.updateOrderStatus(order.getId(), "DELIVERED");

        // ✅ update in-memory object (STATE PATTERN)
        order.setState(new DeliveredState());

        // ✅ notify observers
        order.notifyObservers();
    }
}
