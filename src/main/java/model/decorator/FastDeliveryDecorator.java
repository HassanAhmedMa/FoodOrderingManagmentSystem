package model.decorator;


public class FastDeliveryDecorator implements OrderCost {
    private OrderCost cost;
    public FastDeliveryDecorator(OrderCost cost) { this.cost = cost; }
    public double cost() { return cost.cost() + 30; }
}