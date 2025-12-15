package model.state;


import model.order.Order;


public class DeliveredState implements OrderState {
    public void next(Order order) { System.out.println("Order delivered"); }
    public String getStatus() { return "DELIVERED"; }
}