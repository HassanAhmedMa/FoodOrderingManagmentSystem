package model.state;

import model.order.Order;

public class PreparingState implements OrderState {

    @Override
    public void next(Order order) {
        order.setState(new DeliveredState());
    }

    @Override
    public String getStatus() {
        return "PREPARING";
    }
}
