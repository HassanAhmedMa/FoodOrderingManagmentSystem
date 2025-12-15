package model.observer;


import model.order.Order;


public interface Observer {
    void update(Order order);
}