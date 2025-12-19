package model.order;

import model.order.Order;
import model.order.OrderItem;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.state.OrderState;
import model.user.Customer;
import model.user.DeliveryStaff;

public class ConcreteOrderBuilder implements OrderBuilder {

    private Order order;

    @Override
    public void createOrder() {
        order = new Order();
    }

    @Override
    public void buildId(int id) {
        order.setId(id);
    }

    @Override
    public void buildCustomer(Customer customer) {
        order.setCustomer(customer);
    }

    @Override
    public void buildRestaurant(Restaurant restaurant) {
        order.setRestaurant(restaurant);
    }

    @Override
    public void buildDeliveryStaff(DeliveryStaff staff) {
        order.setDeliveryStaff(staff);
    }

    @Override
    public void buildItem(OrderItem item) {
        order.addItem(item);
    }

    @Override
    public void buildPaymentStrategy(PaymentStrategy strategy) {
        order.setPaymentStrategy(strategy);
    }

    @Override
    public void buildDeliveryAddress(String address) {
        order.setDeliveryAddress(address);
    }

    @Override
    public void buildState(OrderState state) {
        order.setState(state);
    }

    @Override
    public Order getResult() {
        return order;
    }
}
