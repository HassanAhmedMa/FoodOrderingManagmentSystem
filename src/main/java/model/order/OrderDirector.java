package model.order;

import model.order.Order;
import model.order.OrderItem;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.state.PlacedState;
import model.user.Customer;

import java.util.List;

public class OrderDirector {

    private OrderBuilder builder;

    public void setBuilder(OrderBuilder builder) {
        this.builder = builder;
    }

    public Order constructBasicOrder(
            int id,
            Customer customer,
            Restaurant restaurant,
            List<OrderItem> items,
            PaymentStrategy paymentStrategy,
            String address
    ) {

        builder.createOrder();
        builder.buildId(id);
        builder.buildCustomer(customer);
        builder.buildRestaurant(restaurant);

        for (OrderItem item : items) {
            builder.buildItem(item);
        }

        builder.buildPaymentStrategy(paymentStrategy);
        builder.buildDeliveryAddress(address);
        builder.buildState(new PlacedState());

        return builder.getResult();
    }
}
