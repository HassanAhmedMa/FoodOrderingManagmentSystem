package model.observer;


import model.order.Order;


public class CustomerObserver implements Observer {
    public void update(Order order) {
        System.out.println("Order #" + order.getId() + " status: " + order.getStatus());
    }
}