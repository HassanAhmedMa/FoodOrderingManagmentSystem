package model.order;

import model.observer.Observer;
import model.observer.Subject;
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

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void pay(double amount) {
        paymentStrategy.pay(amount);
    }

    public void assignDelivery(DeliveryStaff staff) {
        this.deliveryStaff = staff;
        System.out.println("Assigned to delivery: " + staff.getName());
    }

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

    public int getId() {
        return id;
    }

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
