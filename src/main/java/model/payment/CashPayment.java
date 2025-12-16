package model.payment;

public class CashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Paid by cash: " + amount);
    }

    @Override
    public String getMethod() {
        return "CASH";
    }
}
