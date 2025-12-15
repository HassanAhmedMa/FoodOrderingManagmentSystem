package model.payment;


public class CardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid by card: " + amount);
    }
}