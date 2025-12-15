package model.command;


import model.order.Order;


public class PlaceOrderCommand implements Command {
    private Order order;
    public PlaceOrderCommand(Order order) { this.order = order; }
    public void execute() {
        System.out.println("Order placed");
        order.nextState();
    }
}