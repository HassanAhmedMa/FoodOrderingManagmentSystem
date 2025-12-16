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

    private int id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryStaff deliveryStaff;

    private List<OrderItem> items = new ArrayList<>();
    private PaymentStrategy paymentStrategy;

    private OrderState state = new PlacedState();
    private List<Observer> observers = new ArrayList<>();

    public Order(int id, Customer customer, Restaurant restaurant) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
    }

    /* =====================
       Order Items
       ===================== */
    public List<OrderItem> getItems() {
        return items;
    }
    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    /* =====================
       Payment (FINAL)
       ===================== */

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void payOrder() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }

        // 1️⃣ Create Payment (matches DAO)
        Payment payment = new Payment(this.id, paymentStrategy);

        // 2️⃣ Execute payment
        payment.pay(calculateTotal());

        // 3️⃣ Persist payment
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.createPayment(
                payment.getOrderId(),
                payment.getMethod(),
                payment.getPaymentStatus()
        );
    }

    /* =====================
       Delivery
       ===================== */

    public void assignDelivery(DeliveryStaff staff) {
        this.deliveryStaff = staff;
        System.out.println("Assigned to delivery: " + staff.getFullName());
    }

    /* =====================
       State Pattern
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
       Getters
       ===================== */

    public int getId() {
        return id;
    }

    /* =====================
       Observer Pattern
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setStatus(String delivered) {
    }
}
