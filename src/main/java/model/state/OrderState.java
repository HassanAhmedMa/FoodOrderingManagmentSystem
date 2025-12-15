package model.state;


import model.order.Order;


public interface OrderState {
    void next(Order order);
    String getStatus();
}