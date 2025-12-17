package model.state;

import model.order.Order;

public class OutForDeliveryState implements OrderState {

    @Override
    public void next(Order order) {
        // Delivery staff moves OUT_FOR_DELIVERY → DELIVERED
        order.setState(new DeliveredState());
    }

    @Override
    public String getStatus() {
        return "OUT_FOR_DELIVERY";
    }
}
