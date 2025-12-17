package model.state;

import model.order.Order;

public class ConfirmedState implements OrderState {

    @Override
    public void next(Order order) {
        // Restaurant can move CONFIRMED → PREPARING
        order.setState(new PreparingState());
    }

    @Override
    public String getStatus() {
        return "CONFIRMED";
    }
}
