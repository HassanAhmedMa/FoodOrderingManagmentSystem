package model.facade;

import model.builder.OrderBuilder;
import model.order.Order;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.user.Customer;

public class OrderFacade {

    public Order createOrder(
            int id,
            Customer customer,
            Restaurant restaurant,
            PaymentStrategy payment
    ) {
        return new OrderBuilder(id, customer, restaurant)
                .payment(payment)
                .build();
    }
}
