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
       CONSTRUCTOR
       ===================== */

    public Order(int id, Customer customer, Restaurant restaurant) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
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

    /** ✅ FIX FOR YOUR ERROR */
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
}
