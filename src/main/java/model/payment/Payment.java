package model.payment;

/**
 * Matches PaymentDAO exactly
 */
public class Payment {

    private int orderId;
    private String method;
    private String paymentStatus;

    private PaymentStrategy strategy;

    public Payment(int orderId, PaymentStrategy strategy) {
        this.orderId = orderId;
        this.strategy = strategy;
        this.method = strategy.getMethod();
        this.paymentStatus = "PENDING";
    }

    public void pay(double amount) {
        strategy.pay(amount);
        this.paymentStatus = "SUCCESS";
    }

    /* ===== Getters for DAO ===== */

    public int getOrderId() {
        return orderId;
    }

    public String getMethod() {
        return method;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
