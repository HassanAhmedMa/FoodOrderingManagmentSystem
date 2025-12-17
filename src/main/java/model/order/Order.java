package model.order;

import dao.PaymentDAO;
import model.observer.Observer;
import model.observer.Subject;
import model.payment.Payment;
import model.payment.PaymentStrategy;
import model.restaurant.Restaurant;
import model.state.OrderState;
import model.state.PlacedState;
import model.user.Customer;
import model.user.DeliveryStaff;

import java.util.ArrayList;
import java.util.List;

public class Order implements Subject {

    /* =====================
       CORE FIELDS
       ===================== */

    private int id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryStaff deliveryStaff;

    private List<OrderItem> items = new ArrayList<>();

    /* =====================
       PAYMENT
       ===================== */

    private PaymentStrategy paymentStrategy;

    /* =====================
       DELIVERY
       ===================== */

    private String deliveryAddress;

    /* =====================
       STATE PATTERN
       ===================== */

    private OrderState state = new PlacedState();

    /* =====================
       OBSERVER PATTERN
       ===================== */

    private List<Observer> observers = new ArrayList<>();

    /* =====================
       CONSTRUCTOR (DO NOT REMOVE)
       ===================== */

    public Order(int id, Customer customer, Restaurant restaurant) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
    }

    /* =====================
       BUILDER ENTRY POINT
       ===================== */

    public static Builder builder() {
        return new Builder();
    }

    /* =====================
       ORDER ITEMS
       ===================== */

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    /* =====================
       PAYMENT LOGIC
       ===================== */

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void payOrder() {

        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }

        Payment payment = new Payment(this.id, paymentStrategy);
        payment.pay(calculateTotal());

        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.createPayment(
                payment.getOrderId(),
                payment.getMethod(),
                payment.getPaymentStatus()
        );
    }

    /* =====================
       DELIVERY
       ===================== */

    public void assignDelivery(DeliveryStaff staff) {
        this.deliveryStaff = staff;
        System.out.println("Assigned to delivery: " + staff.getFullName());
    }

    public void setDeliveryAddress(String address) {
        this.deliveryAddress = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /* =====================
       STATE MANAGEMENT
       ===================== */

    public void nextState() {
        state.next(this);
        notifyObservers();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public String getStatus() {
        return state.getStatus();
    }

    /* =====================
       GETTERS
       ===================== */

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

    /* =====================
       OBSERVER IMPLEMENTATION
       ===================== */

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
    }

    /* =========================================================
       BUILDER (ADDED — DOES NOT BREAK EXISTING CODE)
       ========================================================= */

    public static class Builder {

        private int id = -1;
        private Customer customer;
        private Restaurant restaurant;
        private DeliveryStaff deliveryStaff;
        private List<OrderItem> items = new ArrayList<>();
        private PaymentStrategy paymentStrategy;
        private String deliveryAddress;
        private OrderState state = new PlacedState();

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder restaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public Builder deliveryStaff(DeliveryStaff staff) {
            this.deliveryStaff = staff;
            return this;
        }

        public Builder addItem(OrderItem item) {
            this.items.add(item);
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items.addAll(items);
            return this;
        }

        public Builder paymentStrategy(PaymentStrategy strategy) {
            this.paymentStrategy = strategy;
            return this;
        }

        public Builder deliveryAddress(String address) {
            this.deliveryAddress = address;
            return this;
        }

        public Builder state(OrderState state) {
            this.state = state;
            return this;
        }

        public Order build() {

            if (restaurant == null) {
                throw new IllegalStateException("Order must have a restaurant");
            }

            Order order = new Order(id, customer, restaurant);

            order.items.addAll(this.items);
            order.paymentStrategy = this.paymentStrategy;
            order.deliveryAddress = this.deliveryAddress;
            order.deliveryStaff = this.deliveryStaff;
            order.state = this.state;

            return order;
        }
    }
}
