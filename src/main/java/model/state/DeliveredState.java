package model.state;

import model.order.Order;

public class DeliveredState implements OrderState {

    @Override
    public void next(Order order) {
        // Final state, do nothing
    }

    @Override
    public String getStatus() {
        return "DELIVERED";
    }
}
