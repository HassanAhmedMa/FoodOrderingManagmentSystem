package model.builder;


import model.order.Order;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.user.Customer;


public class OrderBuilder {
    private Order order;


    public OrderBuilder(int id, Customer customer, Restaurant restaurant) {
        order = new Order(id, customer, restaurant);
    }


    public OrderBuilder payment(PaymentStrategy strategy) {
        order.setPaymentStrategy(strategy);
        return this;
    }


    public Order build() { return order; }
}