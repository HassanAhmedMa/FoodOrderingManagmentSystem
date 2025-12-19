package model.order;


import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.state.OrderState;
import model.state.PlacedState;
import model.user.Customer;
import model.user.DeliveryStaff;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryStaff deliveryStaff;
    private List<OrderItem> items;
    private PaymentStrategy paymentStrategy;
    private String deliveryAddress;
    private OrderState state;


    /* Constructor used ONLY by builder */
    public Order() {
        this.items = new ArrayList<>();
        this.state = new PlacedState();
    }

    /* ================= SETTERS (USED BY BUILDER) ================= */

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDeliveryStaff(DeliveryStaff deliveryStaff) {
        this.deliveryStaff = deliveryStaff;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    /* ================= SUBJECT ================= */


    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public DeliveryStaff getDeliveryStaff() {
        return deliveryStaff;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public String getStatus() {
        return state.getStatus();
    }
    public OrderState getState() {
        return state;
    }
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }
    public void nextState() {
        state.next(this);
    }




}
