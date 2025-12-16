package model.state;

import model.order.Order;

public class PlacedState implements OrderState {

    @Override
    public void next(Order order) {
        order.setState(new PreparingState());
    }

    @Override
    public String getStatus() {
        return "PLACED";
    }
}
