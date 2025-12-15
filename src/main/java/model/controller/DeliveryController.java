package model.controller;

import model.order.Order;

public class DeliveryController {

    public void deliverOrder(Order order) {
        System.out.println("Delivering order #" + order.getId());
        order.nextState();
    }
}
