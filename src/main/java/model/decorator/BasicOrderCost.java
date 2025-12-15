package model.decorator;

public class BasicOrderCost implements OrderCost {

    private double baseCost;

    public BasicOrderCost(double baseCost) {
        this.baseCost = baseCost;
    }

    @Override
    public double cost() {
        return baseCost;
    }
}
