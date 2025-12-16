package model.payment;

public interface PaymentStrategy {
    void pay(double amount);
    String getMethod();
}
