package model.decorator;


public class ExtraCheeseDecorator implements OrderCost {
    private OrderCost cost;
    public ExtraCheeseDecorator(OrderCost cost) { this.cost = cost; }
    public double cost() { return cost.cost() + 20; }
}