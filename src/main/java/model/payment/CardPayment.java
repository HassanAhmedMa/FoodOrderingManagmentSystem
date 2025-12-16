package model.payment;

public class CardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Paid by card: " + amount);
    }

    @Override
    public String getMethod() {
        return "CARD";
    }
}
