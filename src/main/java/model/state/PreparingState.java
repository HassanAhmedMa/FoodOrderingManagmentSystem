package model.state;


import model.order.Order;


public class PreparingState implements OrderState {
    public void next(Order order) { order.setState(new DeliveredState()); }
    public String getStatus() { return "PREPARING"; }
}