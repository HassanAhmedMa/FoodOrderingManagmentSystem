package model.state;


import model.order.Order;


public class PlacedState implements OrderState {
    public void next(Order order) { order.setState(new PreparingState()); }
    public String getStatus() { return "PLACED"; }
}